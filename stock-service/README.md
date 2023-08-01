# stock-service
The Stock Service is a REST CRUD API that allows clients to manage stocks, service is implemented in Java using the Spring Boot framework (H2 in-memory Database, Liquibase for migration and OpenAPI 3).

# Getting Started
# Prerequisites
To run the matching-service, you need to have the following software installed on your system:

* Java 11
* Maven

# Installing
To install the stock-service, follow these steps:
* Clone the repository to your local machine [Repo](https://github.com/mohammedayad/tech-demos.git)
* Navigate to the project directory: stock-service folder
* Build the project using Maven and Run on the command line:

This project is a [Maven](https://maven.apache.org/) project. Apache Maven is a very widely used project management tool for Java projects. Most Java IDEs, such as IntelliJ
IDEA and Eclipse, understand Maven projects, so it should be easy to open the project in your Java IDE.

If you want or need to work with the project in a terminal window or in the Windows command prompt or PowerShell, then you can use the following commands to compile the code and
run the unit tests. The unit tests will check if you've successfully solved the exercises.

In the terminal, Windows command prompt or PowerShell, first use the `cd` command to go to the directory where you've unzipped this project. Then use one of the following commands:

On Windows:

    mvnw.cmd clean package

On macOS or Linux:

    ./mvnw clean package

These commands will download Apache Maven if you haven't already have it installed, and then run Maven to compile the code and run the unit tests.

If everything compiles without errors and all the tests succeed, the command will finish with a message that says "BUILD SUCCESS".

To compile the code without running the unit tests, add the `-DskipTests` option:

On Windows:

    mvnw.cmd clean package -DskipTests

On macOS or Linux:

    ./mvnw clean package -DskipTests
    
# Running
    mvn spring-boot:run
    
## Testing

The application includes both unit tests and integration tests to ensure that it works correctly.
    
# Another way to run it using [Docker](https://hub.docker.com/repository/docker/mohammedayad/matching-service/general)
    Pull the Docker image from Docker Hub: docker pull mohammedayad/stock-service:v1
    Run the Docker container: docker run -p 8081:8081 mohammedayad/stock-service:v1

# OpenAPI (stock-service API documentation ) 
    [Swagger UI](http://localhost:8081/swagger-stock-service-ui.html)
    [API Docs](http://localhost:8081/api-docs)

# The service will be available at:
    http://localhost:8081/stock-service/v1/api/stocks
    
# API Reference
The Stock Service provides the following REST API endpoints:
* GET /stock-service/v1/api/stocks?page=4&size=10: Retrieves a paginated list of all stocks.
* GET /stock-service/v1/api/stocks/{id}: Retrieves a single stock by ID.
* POST /stock-service/v1/api/stocks: Create a new stock record.
* PATCH /stock-service/v1/api/stocks/{id}: Update the price of a stock by ID.
* DELETE /stock-service/v1/api/stocks/{id}: Deletes a single stock by ID.

# Exception Handling
The Stock Service uses exception handling to handle errors that occur during the processing of requests. If an error occurs, the service will return an appropriate HTTP status code and an error message in the response body Using Problem Schema.
* StockNotFoundException: Thrown when the Stock not found. This exception results in an HTTP 404 (NOT FOUND) response.
* IllegalArgumentException: Thrown when the request has Invalid request body. This exception results in an HTTP 400 (Bad Request) response.
* Exception: Thrown when any other unhandled exception occurs during the processing of requests. This exception results in an HTTP 500 (Internal Server Error) response, and a generic error message returned in the response body.


# Built With
* Java 11 - Programming language
* Spring Boot - Web framework
* Maven - Build tool
* OpenAPI 3 (Code First)
* Liquibase
* H2 Database
* Junit
* Mockito
* Docker


# Authors
* Mohammed Ayad
