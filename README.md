# Customer Banking Application Setup

## Prerequisites

Ensure you have Docker Desktop installed on your system which will install Docker Engine, Docker CLI client, and Docker Compose.

## Getting Started

Follow the instructions below to set up and run the customer banking application using Docker Compose :

### 1. Getting the project, Building and Starting the application

To build the application image, create the network, and start both the application and database containers for the first
time, use the following command:

   ```bash
   git clone https://github.com/ManishaKanyal21/customer-banking-app.git
   cd customer-banking-app
   docker-compose up
   ```

This command automatically leverages the configurations provided in the docker-compose.yml file to build the necessary
images and start the services.

### 2. Updating and Rebuilding the Application :

Docker Compose efficiently caches images. When we make changes to our local code, we must explicitly tell Compose to
rebuild the application image.

Choose one of the following methods:

**Option A: Force a rebuild while starting (Recommended for development)**

This single command ignores the cache, rebuilds the customer-banking-image image with the latest code, and starts the
containers:

 ```bash   
docker-compose up --build
```

**Option B: Manual Build followed by Run**

1. Build the image manually (ensure to use the exact tag from the docker-compose.yml file):

 ```bash

docker build -t customer-banking-image .

```

2. Start the containers (Compose will use the image we just built):

```bash 
   docker-compose up
```

### 3. Accessing the API Documentation :

Once the containers have started successfully (check the terminal output for messages like "Started Application..."
and "Database connected..."), we can access the API documentation:

**Swagger UI:** http://localhost:8080/swagger-ui.html

**OpenAPI Spec (JSON)**: http://localhost:8080/v3/api-docs

### 4. Stopping the application :

To stop the running containers and delete the volume: 

```bash 
   docker-compose down -v
```

   