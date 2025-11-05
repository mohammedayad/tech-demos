# City Sorter Service

A Spring Boot application that groups and sorts city names from files in a directory using parallel processing. Cities are grouped by their sorted character sequences and written to an output file.

## Getting Started

This service reads city names from files in a specified directory, groups them by alphabetical character sequence, sorts the groups, and writes the results to an output file. The solution is containerized using Docker for easy deployment.

## Prerequisites
To run the City Sorter Service, you need to have the following software installed on your system:

* Java 17
* Maven

## Installing

To install the City Sorter Service, follow these steps:
* Clone the repository to your local machine [Repo](https://github.com/TWTG-R-D-B-V/software-development-assessment-mohammedayad.git)
* Navigate to the project directory: city-sorter folder
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
1. Create necessary directories: `mkdir -p cities-storage output`
2. Add city files to cities-storage directory (one city per line)
3. Start the service using Docker Compose: `docker-compose up --build`
4. The service will:
   * Expose port 8080.
   * Mount ./cities-storage as input directory.
   * Mount ./output for results.
   * Create output file at output/city-sorter-output.txt

## Testing
run `mvn test`

**Note:** The Docker build skips tests by default (-DskipTests)

## Exception Handling
Custom exceptions (`CitySorterServerException`) handle all the errors and all exceptions are logged with detailed context using SLF4J.

## Configuration

* `city-sorter.citiesStorageFolder`: Input directory path (default: classpath:cities-storage)

* `city-sorter.citiesStorageOutputFileName`: Output filename (default: city-sorter-output.txt)

**Note:** the two properties can be set with the following Environment variable `CITIES_STORAGE_FOLDER` and `CITY_SORTER_OUTPUT_FILE`

* Thread Pool Configuration:
  * async-executor.corePoolSize=7
  * async-executor.maxPoolSize=42
  * async-executor.queueCapacity=11
  * async-executor.keepAliveTime=60
  * async-executor.threadNamePrefix=Async-Executor-


# Built With
* Java 17
* Spring Boot 3
* Maven
* Junit
* Mockito
* Docker/Docker Compose

# Authors
* Mohammed Ayad








