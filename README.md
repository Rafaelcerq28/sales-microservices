# Product Store - Microservices with Spring Boot

This project is a microservices-based REST API for a product store, developed using Java. It leverages Spring Boot, Spring Cloud, Eureka Naming Server, and Spring Cloud Gateway to provide features like product registration, user registration, and sales processing.

## Microservices

The project consists of three main microservices:

1. **Product-Service:** Responsible for managing the products available in the store.

2. **User-Service:** Handles user registration for the platform.

3. **Product-Sales:** Manages the sales process, including order checkout and payments.

## Configuration

Before running the microservices, make sure you have the following prerequisites installed:

- Java 8 or above
- Maven
- 
## Running the Project

1. Clone this repository to your local machine:

   ```bash
   git clone https://github.com/Rafaelcerq28/sales-microservices.git
   ```

2. Navigate to the project's root directory:

   ```bash
   cd sales-microservices
   ```

3. Run each microservice individually:
   
    1. naming-server
    2. api-gateway
    3. product-service
    4. user-service
    5. product-sales

   - To run a microservice individually, navigate to the microservice's folder and use Maven:

Ex:
     ```bash
     cd naming-server
     mvn spring-boot:run
     ```
- The other microservices can be started individually following the same pattern.

5. The microservices will be available on the following ports:

   - Product Registration: http://localhost:8081
   - User Registration: http://localhost:8082
   - Sales: http://localhost:8083
   - Eureka Naming Server: http://localhost:8761
   - Spring Cloud Gateway: http://localhost:8765

## API Documentation

For details about the endpoints and how to use the API, refer to the documentation available in each microservice. For example:

- Product Registration Documentation: http://localhost:8081/swagger-ui.html
- User Registration Documentation: http://localhost:8082/swagger-ui.html
- Sales API Documentation: http://localhost:8083/swagger-ui.html

## Contributing

If you wish to contribute to this project, feel free to submit pull requests. We welcome improvements and bug fixes.
