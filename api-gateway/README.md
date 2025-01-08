# api-gateway
The api gateway is a spring gateway, service is implemented in Java using the Spring Boot framework.

# Getting Started
# Prerequisites
To run the api-gateway, you need to have the following software installed on your system:

* Java 17
* Maven

# Installing
To install the api-gateway, follow these steps:
* Clone the repository to your local machine [Repo](https://github.com/mohammedayad/tech-demos.git)
* Navigate to the project directory: api-gateway and integration-service folders
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

* the service has a gateway route for testing available: http://localhost:8080/integration-service/v1/api/test
* make sure integration-service is up and running
* The application includes sample integration tests to ensure that it works correctly.


# Built With
* Java 17 - Programming language
* Spring Boot - Web framework
* spring cloud gateway
* micrometer for observability
* bucket4j for rate limiting
* Maven - Build tool


# Authors
* Mohammed Ayad