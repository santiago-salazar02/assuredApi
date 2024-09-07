# API Testing Project

This project is designed to perform CRUD operations testing on resources and clients using Cucumber and RestAssured.

## Prerequisites

- Java 17
- Maven

## Dependencies

- Cucumber.
- Faker.
- Lombok.
- Gson.
- Rest assured.
- Junit.
- Log4j

## Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/santiago-salazar02/assuredApi
    cd assuredApi.git
    ```

2. Install the dependencies:
    ```sh
    mvn clean install
    ```

## Running the Tests

To run the tests, use the following Maven command:
```sh
mvn test
```
## Project Structure

1. Resources:
   1. Get the list of active resources.
   2. Update the latest resource.
2. Clients
   1. Change the phone number of the first client named Laura.
   2. Update and delete a new client.