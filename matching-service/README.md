# matching-service
The Prefix Matching Service is a REST API that allows clients to search for the longest prefix that matches a given input string from a list of predefined prefixes. 
The service is implemented in Java using the Spring Boot framework (H2 in-memory Database to save Prefixes and Trie Algorithm).

# Getting Started
# Prerequisites
To run the matching-service, you need to have the following software installed on your system:

* Java 8 or later
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

# Authors
* Mohammed Ayad
