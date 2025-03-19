## Required Configuration

Before running the Docker Compose setup, you must configure the following variables in your `application.properties`
file:

```properties
admin.password=your_admin_password
admin.email=your_admin_email
spring.mail.username=your_mail_username
spring.mail.password=your_mail_password
```

These variables are required for the application to function properly. Replace the placeholders with your actual values.

## Getting Started

1. Clone this repository
2. Configure the required variables in `application.properties`
3. Build and run the application using Docker Compose:

```bash
docker-compose up --build
```

## Services

The Docker Compose setup includes the following services:

- **Spring Boot Application**: Java 17 application running on port 8080
- **PostgreSQL**: Database for the development profile
- **Redis**: In-memory data structure store used by the application

## Accessing the Application

Once the containers are running, you can access the application at:

```
http://localhost:8080
```

## Stopping the Application

To stop all services, run:

```bash
docker-compose down
```

To stop all services and remove volumes (will delete all data), run:

```bash
docker-compose down -v