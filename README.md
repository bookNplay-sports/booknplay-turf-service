Turf Service is a microservice of the BookNPlay Sports Turf Booking Platform. It is responsible for managing sports turf details, including adding new turfs, updating turf information, retrieving turf details, and managing turf availability. It allows turf owners to manage their turfs and enables customers to view available turfs for booking. The service communicates with other microservices through REST APIs and uses Eureka for service discovery.

Technology Stack: Java 21, Spring Boot, Spring Data JPA, Spring Security, MySQL, Maven, and Eureka Client.

Configuration: Service Name: turf-service | Port: 8083 | Database: turf_db | Eureka Server: http://localhost:8761

The service is part of the BookNPlay microservices architecture and integrates with other services for turf and booking-related operations.
