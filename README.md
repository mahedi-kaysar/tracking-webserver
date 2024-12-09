# Tracking Web Server

This project implements a small web server using Spring Boot. The server provides two endpoints:
- `/ping`: Returns a response code 200 and string "OK" when the file `/tmp/ok` is present. If the file is not present, it returns a 503 Service Unavailable.
- `/img`: Returns a 1x1 GIF image and logs the request.

## Features

- **Ping Endpoint**: Checks the presence of a file and returns the appropriate status.
- **Image Endpoint**: Serves a 1x1 GIF image and logs the request.
- **Thread Pool Configuration**: Manages concurrent requests efficiently using a thread pool. Both endpoints are non-blocking.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Docker (optional, for containerization)

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/tracking-web-server.git
   cd tracking-web-server
   ```

2. Build the project:
   ```sh
   mvn clean package
   ```

3. Run the application:
   ```sh
   mvn spring-boot:run
   ```

### Docker

To run the application in a Docker container:

1. Build the Docker image:
   ```sh
   docker build -t tracking-web-server .
   ```

2. Run the Docker container:
   ```sh
   docker run -p 8080:8080 tracking-web-server
   ```

## Endpoints

### `/ping`

- **Description**: Checks the presence of the file `/tmp/ok`.
- **Response**:
   - `200 OK` if the file is present.
   - `503 Service Unavailable` if the file is not present.

### `/img`

- **Description**: Returns a 1x1 GIF image.
- **Response**:
   - `200 OK` with the image if found.
   - `404 Not Found` if the image is not found.

## Logging

The application uses SLF4J with Logback for logging. Logs are written to the console.

## Testing

The project includes unit tests for the endpoints using JUnit and MockMvc. To run the tests:

```sh
mvn test
```

## Future Improvements

1. Enhanced Error Handling: Improve error handling to provide more detailed error messages and logging. 
2. Configuration Management: Externalize configuration properties to allow easier customization and deployment.
3. Performance Optimization: Optimize the application for better performance and scalability.
4. API Versioning: Implement versioning for APIs to manage changes and backward compatibility.
5. Integration Tests: Add integration tests to validate the application behavior with external dependencies.
6. Containerization: Improve Dockerfile for better image size and security.
7. CI/CD Pipeline: Implement a CI/CD pipeline for automated testing and deployment.
8. Monitoring and Alerting: Integrate with monitoring tools to track application performance and receive alerts.
9. Documentation: Enhance documentation with detailed usage examples and API specifications.
10. Load Balancing: Implement load balancing to distribute traffic across multiple instances of the application.
11. Security Enhancements: Implement security measures such as HTTPS, authentication, and authorization to protect endpoints

## Acknowledgements

- Spring Boot
- SLF4J and Logback
- JUnit and MockMvc