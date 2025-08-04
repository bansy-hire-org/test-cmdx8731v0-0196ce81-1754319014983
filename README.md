# Order Management Microservice

A simple order management microservice built with Spring Boot.

## Prerequisites

*   Java 21
*   Maven
*   Docker (optional, for containerization)
*   Kubernetes (optional, for deployment)

## Building and Running

1.  Clone the repository.
2.  Build the application using Maven: `mvn clean install`
3.  Run the application: `java -jar target/order-management-1.0.0.jar`

## API Endpoints

*   `GET /api/orders`: Get all orders.
*   `GET /api/orders/{id}`: Get an order by ID.
*   `POST /api/orders`: Create a new order.
*   `PUT /api/orders/{id}`: Update an existing order.
*   `DELETE /api/orders/{id}`: Delete an order.
