# holidays-service
A Spring Boot microservice for retrieving and managing holiday information from the Nager.Date API. Provides endpoints for retrieving last celebrated holidays, non-weekend holiday counts, and common holidays between countries.

## Features
- Retrieve last celebrated holidays for a country
- Calculate non-weekend holiday counts for multiple countries
- Find common holidays between two countries
- Input validation and error handling
- OpenAPI documentation
- Docker containerization


# Prerequisites
To run the holidays-service, you need to have the following software installed on your system:

* Java 17
* [Maven](https://maven.apache.org/)
* [Docker](https://www.docker.com/) (optional)
* [Docker Compose](https://docs.docker.com/compose/) (optional)

## Getting Started
# Installing
To install the holidays-service, follow these steps:
* Clone the repository to your local machine [Repo](https://github.com/mohammedayad/tech-demos.git)
* Navigate to the project directory: holidays-service folder
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
* Start the service using Docker Compose: `docker-compose up --build`
* OR using maven `mvn spring-boot:run`
    
## Testing
run `mvn test`
**Note:** The application includes both unit tests and integration tests to ensure that it works correctly and The Docker build skips tests by default (-DskipTests).

# OpenAPI (holidays-service API documentation ) 
    [Swagger UI](http://localhost:8080/swagger-holidays-service-ui.html)
    [API Docs](http://localhost:8080/api-docs)

# The service will be available at:
    http://localhost:8080

# API Reference
The Holidays Service provides the following REST API endpoints:
* GET /holidays-service/v1/api/last-celebrated-holidays/{numberOfHolidays}/{countryCode}: Retrieves last celebrated holidays by number of holidays and country code.
  * Example: http://localhost:8080/holidays-service/v1/api/last-celebrated-holidays/3/NL
* GET /holidays-service/v1/api/non-weekend-holidays/{year}?countryCodes= : Retrieves Non Weekend Holidays for specific Year and specific Country Codes.
  * Example: http://localhost:8080/holidays-service/v1/api/non-weekend-holidays/2025?countryCodes=NL,AT,EG,XX
  * unrecognised country code will have -1 value.
* GET /holidays-service/v1/api/common-holidays/{year}?countryCode1=&countryCode2=: Create a new stock record.
  * Example: http://localhost:8080/holidays-service/v1/api/common-holidays/2025?countryCode1=NL&countryCode2=AT

# Exception Handling
The holidays-service uses exception handling to handle errors that occur during the processing of requests. If an error occurs, the service will return an appropriate HTTP status code and an error message in the response body Using Problem Schema.
* ServiceProcessingException: Thrown when API communication fails. This exception results in an HTTP 404 (NOT FOUND) or HTTP 400 (Bad Request) response.
* IllegalArgumentException: Thrown when the request has Invalid request. This exception results in an HTTP 400 (Bad Request) response.
* Exception: Thrown when any other unhandled exception occurs during the processing of requests. This exception results in an HTTP 500 (Internal Server Error) response, and a generic error message returned in the response body.

## Configuration
Environment variables (set in docker-compose.yml):

* `holiday-api-client.url`: Holiday API client URL

* `holiday-api-client.year`: Holiday API client Default Year

**Note:** the two properties can be set with the following Environment variable `HOLIDAY_API_CLIENT_URL` and `HOLIDAY_API_CLIENT_YEAR`

* Thread Pool Configuration:
  * async-executor.corePoolSize=7
  * async-executor.maxPoolSize=42
  * async-executor.queueCapacity=11
  * async-executor.keepAliveTime=60
  * async-executor.threadNamePrefix=Async-Executor-

# Built With
* Java 17
* Spring Boot 3
* Maven - Build tool
* OpenAPI 3 (Code First)
* Junit
* Mockito
* Docker/Docker Compose


# Authors
* Mohammed Ayad
