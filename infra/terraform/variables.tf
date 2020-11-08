// Config
variable "project" {
  type        = string
  description = "Project ID"
}

variable "region" {
  type    = string
  default = "us-central1"
}

variable "zone" {
  type    = string
  default = "us-central1-a"
}

// Service Name
variable "vpc_name" {
  type    = string
  description = "Name of the vpc"
  default = "kitchen-vpc-dev"
}

variable "vpc_subnet_name" {
  type    = string
  description = "Name of the vpc subnetwork"
  default = "kitchen-vpc-dev-subnet"
}

variable "gke_cluster_name" {
  type    = string
  description = "Name of the GKE cluster"
  default = "kitchen-dev"
}

variable "gke_node_pool_name" {
  type    = string
  description = "Name of the GKE node pool"
  default = "my-node-pool"
}

variable "database_instance_name" {
  type    = string
  description = "Name of the DB instance"
  default = "kitchen-db-dev-"
}

