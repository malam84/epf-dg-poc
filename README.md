Spring Boot Caching with Red Hat Data Grid & PostgreSQL

This project is a Spring Boot application that integrates with Red Hat Data Grid (Remote Cache) and PostgreSQL for caching and database operations.

Features

    1. Red Hat Datagrid Session Caching
    
    2. Red Hat Datagrid API response Caching
    
    3. Write-Behind & Read-Through Caching

Stack 
    1. Spring Boot with JPA & Hibernate
    2. Infinispan (Red Hat Data Grid) for Caching
    3. PostgreSQL as the database
    4. Spring Boot REST APIs

Prerequisites
   Ensure you have the following installed:
     1. Java 17+
     2. Maven 3+
     3. Docker (for PostgreSQL and Data Grid, if running locally)
     4. Red Hat Data Grid (Remote Server)

Setup Instructions

1️⃣ Clone the Repository
      git clone https://github.com/phpavan/DataGrid.git
      cd <project-directory>

2️⃣ Configure Database
    
      Option 1: Run PostgreSQL in Docker
    
          docker run --name postgres-db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=mydb -p 5432:5432 -d postgres
    
          Update application.properties to match your database config.
    
      Option 2: Use an Existing PostgreSQL Instance
    
          Modify the spring.datasource.url in application.properties:
    
          spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
          spring.datasource.username=admin
          spring.datasource.password=admin

3️⃣ Configure Red Hat Data Grid (Remote)

    If running locally via Docker:
  
    docker run -d -p 11222:11222 --name datagrid-server quay.io/infinispan/server:latest

    Create 3 Caches in Datagrid with below specs:

        Cache : session

            {
              "sessions": {
                "distributed-cache": {
                  "owners": "2",
                  "mode": "SYNC",
                  "statistics": true,
                  "encoding": {
                    "media-type": "application/x-protostream"
                  }
                }
              }
            }

        Cache : mockedApiResponse

            {
              "mockedApiResponse": {
                "distributed-cache": {
                  "owners": "2",
                  "mode": "SYNC",
                  "statistics": true,
                  "encoding": {
                    "media-type": "application/json"
                  }
                }
              }
            }

        Cache : users-cache

            {
              "users-cache": {
                "distributed-cache": {
                  "owners": "2",
                  "mode": "SYNC",
                  "statistics": true,
                  "encoding": {
                    "media-type": "application/json"
                  }
                }
              }
            }
            
    Ensure Infinispan/Remote Data Grid is properly configured in application.properties:
  
    infinispan.remote.server-list=localhost:11222

4️⃣ Build & Run the Application

    Build the project:
  
    mvn clean install
  
    Run the project:

      mvn spring-boot:run

5️⃣ Testing the APIs

    Once the application is running, access the APIs via:

    Swagger UI: http://localhost:8080/swagger-ui/index.html

<img width="1445" alt="image" src="https://github.com/user-attachments/assets/79baf35a-dda4-4f89-9e33-27d112f6062c" />

    Example Endpoints:
       1. To Test Session Caching
             curl -X POST http://localhost:8080/session/set?key=username&value=JohnDoe
              You can see Springboot session & attributes are cached in Datagrid server, In Cache named: "sessions"
             curl -X GET http://localhost:8080/session/get?key=username
              You can see Springboot session & attributes are queried from Datagrid server. Even if you restart the Springboot service, you can retrieve previous session.
              
       2. To test API Caching
            curl -X GET http://localhost:8080/fetch-data?id=123
              You can see json response from the Rest API is cached in Datagrid server, In Cache named: "mockedApiResponse"

<img width="1456" alt="image" src="https://github.com/user-attachments/assets/b93cf001-66e6-4930-bb88-d2b257b412b3" />

       3. To test DB Caching (Read Through & WriteBehind)
            curl -X GET http://localhost:8080/users/2
            curl -X POST http://localhost:8080/users
             body - {"email": "sam@rh.com", "name": "Sam Altman", "version": 1}

<img width="1451" alt="image" src="https://github.com/user-attachments/assets/ad205265-5bb1-4565-913b-0790981c6af2" />


