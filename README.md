# API Explorer for SpaceX API
Very simple RESTful API that returns aggregated responses of the SpaceX RESTful API

### Technologies
- Java 11
- Spring Boot 2.2 
- Maven 3+

### Run
`./mvnw spring-boot:run`

### API
- `GET` http://localhost:8080/api/rockets
    - returns summary of all rockets
    
- `GET` http://localhost:8080/api/rockets/{rocketId}
    - returns details about a single rocket, past and upcoming launches