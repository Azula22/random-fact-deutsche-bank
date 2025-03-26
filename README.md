# Fact Service

This is a Backend service that fetches random facts, shortens URLs, caches them, and provides access statistics.

## Running the Project in Docker

To run this project inside a Docker container, follow these steps:

```sh
./gradlew buildFatJar
docker build -t ktor-app .
docker run -p 8080:8080 ktor-app
```
Once the container is running, you can access the service at: 🔗 http://0.0.0.0:8080

## Building & Running 

To build or run the project, use one of the following tasks:

| Task              | Description                                                          |
|-------------------|---------------------------------------------------------------------- |
| `./gradlew test`  | Run the tests                                                        |
| `./gradlew build` | Build everything                                                     |
| `./gradlew run`   | Run the server                                                       |

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```



