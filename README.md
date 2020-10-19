# Kitchen

A web application that helps you find the best recipe that uses ingredients in your refrigerator.

## Technologies
- Java 11
- [Spring Boot](https://spring.io/projects/spring-boot) 2.3.1 (with Gradle)
- Spring Data JPA
- MySQL 5.7
- [Thymeleaf](https://www.thymeleaf.org/)
- [Bootstrap](https://getbootstrap.com/) 4.5 & jQuery
- Docker (For MySQL envirnoment)
- [DbSetup](http://dbsetup.ninja-squad.com) 2.1.0 (For testing)
- [Lombok](https://projectlombok.org) 1.18.12

## How to run
1. Clone this repository / Import this repository in your IDE. 
2. Go to this repository directory.
3. Start MySQL by `docker-compose up -d` (default port: 3307).
4. Run this project by `./gradlew bootRun` or IDE Run Button. 
5. Visit `http://localhost:8080` 

## How To use
1. Create categories for ingredients.
2. Create your ingredients.
3. Create recipes.

Now you can make a dish by choose your favorite ingredients or by following the recipes.

## License

This project is licensed under the MIT license.