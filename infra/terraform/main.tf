provider "google" {
  region = var.region
  project = var.project
  zone    = var.zone
}

// VPC
resource "google_compute_network" "vpc" {
  name                    = var.vpc_name
  auto_create_subnetworks = false
}

// subnetwork
resource "google_compute_subnetwork" "vpc_subnet" {
  name = var.vpc_subnet_name
  ip_cidr_range = "10.9.0.0/16" // custom ip address range
  network = google_compute_network.vpc.name
}

// firewall
resource "google_compute_firewall" "kitchen-vpc-firewall-http" {
  name        = "kitchen-vpc-firewall-http"
  description = "Allow tcp 80 traffic"
  network     = google_compute_network.vpc.name

  allow {
    protocol = "icmp"
  }

  allow {
    protocol = "tcp"
    ports    = ["80"]
  }
}

// Private IP
resource "google_compute_global_address" "private_ip" {
  name         = "private-ip"
  description  = "A block of private IP addresses that are accessible only from within the VPC."
  purpose      = "VPC_PEERING"
  address_type = "INTERNAL"
  ip_version   = "IPV4"
  prefix_length = 16
  network       = google_compute_network.vpc.name
}

// private service connection
resource "google_service_networking_connection" "private_vpc_connection" {
  network                 = google_compute_network.vpc.name
  service                 = "servicenetworking.googleapis.com"
  reserved_peering_ranges = [google_compute_global_address.private_ip.name]
}

// GKE
resource "google_container_cluster" "gke-cluster-dev" {
  name               = var.gke_cluster_name
//  location           = "us-central1-a"
  initial_node_count = 1

  // We can't create a cluster with no node pool defined, but we want to only use
  // separately managed node pools. So we create the smallest possible default
  // node pool and immediately delete it.
  remove_default_node_pool = true

  network    = google_compute_network.vpc.self_link
  subnetwork = google_compute_subnetwork.vpc_subnet.name

  // to connect the Private IP
  ip_allocation_policy {
  }

  master_auth {
    username = ""
    password = ""
    client_certificate_config {
      issue_client_certificate = false
    }
  }
  master_authorized_networks_config {
    cidr_blocks {
      display_name = "allow_all"
      cidr_block   = "0.0.0.0/0"
    }
  }
}
// custom node pool
resource "google_container_node_pool" "primary_preemptible_nodes" {
  name       = var.gke_node_pool_name
//  location   = "us-central1-a"
  cluster    = google_container_cluster.gke-cluster-dev.name
  node_count = 2

  node_config {
    preemptible  = true
    machine_type = "g1-small"
//    machine_type = "n1-standard-1"

    metadata = {
      disable-legacy-endpoints = "true"
    }

    oauth_scopes = [
      "compute-rw", "logging-write", "monitoring",
      "https://www.googleapis.com/auth/service.management.readonly",
      "https://www.googleapis.com/auth/servicecontrol",
      "https://www.googleapis.com/auth/trace.append",
      "https://www.googleapis.com/auth/cloud-platform",
      "https://www.googleapis.com/auth/sqlservice.admin",
      "storage-rw",
      "https://www.googleapis.com/auth/monitoring",
    ]
  }
}

// Database instance
resource "google_sql_database_instance" "master" {
  deletion_protection = false
  name = "${var.database_instance_name}${random_id.sql_database_suffix.hex}" // same name can not be used before a week after deleted
  database_version = "MYSQL_5_7"
//  region       = "us-central1"


  settings {
    tier = "db-f1-micro"
    disk_type = "PD_HDD"
    disk_size = 10
    location_preference {
      zone = "us-central1-a"
	}

    ip_configuration {
      ipv4_enabled    = false
      require_ssl     = false
      private_network = google_compute_network.vpc.self_link
    }
  }
  depends_on = [
    google_service_networking_connection.private_vpc_connection,
    random_id.sql_database_suffix
  ]
}
// random id
resource "random_id" "sql_database_suffix" {
  byte_length = 8
}

// Database
resource "google_sql_database" "database" {
  name      = "kitchen"
  instance  = google_sql_database_instance.master.name
  charset   = "utf8mb4"
  collation = "utf8mb4_general_ci"
}
// user
resource "google_sql_user" "user" {
  name     = "user"
  instance = google_sql_database_instance.master.name
  host     = "%"
  password = "password"
}