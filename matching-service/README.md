# matching-service
The Prefix Matching Service is a REST API that allows clients to search for the longest prefix that matches a given input string from a list of predefined prefixes. 
The service is implemented in Java using the Spring Boot framework (H2 in-memory Database to save Prefixes and load the prefixes from sample_prefixes.csv, Liquibase for migration and Trie Algorithm).

# Exception Handling
The Prefix Matching Service uses exception handling to handle errors that occur during the processing of requests. If an error occurs, the service will return an appropriate HTTP status code and an error message in the response body.
* MatchingException: Thrown when the input string is invalid or empty or there is not a prefix match the input string. This exception results in an HTTP 400 (Bad Request) response and return error message "No Prefix Matching has been found".
* Exception: Thrown when any other unhandled exception occurs during the processing of requests. This exception results in an HTTP 500 (Internal Server Error) response, and a generic error message returned in the response body.

# Getting Started
# Prerequisites
To run the matching-service, you need to have the following software installed on your system:

* Java 11
* Maven

# Installing
To install the matching-service, follow these steps:
* Clone the repository to your local machine [Repo](https://github.com/mohammedayad/tech-demos.git)
* Navigate to the project directory: matching-service folder
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
    
# another way to run it using [Docker](https://hub.docker.com/repository/docker/mohammedayad/matching-service/general)
    Pull the Docker image from Docker Hub: docker pull mohammedayad/matching-service:v1
    Run the Docker container: docker run -p 8081:8081 mohammedayad/matching-service:v1

   

# The service will be available at:
    http://localhost:8081/api/prefix-matching/v1
    
# API Reference
The Prefix Matching Service provides the following REST API endpoints:
* GET /api/prefix-matching/v1/prefixes: Retrieves all the predefined prefixes.
* GET /api/prefix-matching/v1/longestPrefix/{input}: Retrieves the longest prefix from a list of prefixes that matches the given input string.

# Built With
* Java 11 - Programming language
* Spring Boot - Web framework
* Maven - Build tool
* Liquibase
* H2 Database
* Junit
* Mockito
* Docker

# Note
* I have implemented another approach rather than using Trie Algorithm,
   I used Database Directly to match the possible matching using Database query and will retrieve the result ordered by the largest length, then I can retrieve the first match.
   but this approach will depend on the DB performance itself and will need more tuning in large Prefixes Dataset.
   I was trying to get a simple way to avoid loading the large Data in memory (we can have other solutions for the caching) to use the Trie Algorithm as I was loading the prefixes in the application startup.
   so you can check the other approach too.
# using DB approach
# API Reference
The Prefix Matching Service by using the DB provides the following REST API endpoints:
* GET /api/prefix-matching/v1/longestPrefix/DB/{input}: Retrieves the longest prefix from a list of prefixes that matches the given input string.

# Authors
* Mohammed Ayad
