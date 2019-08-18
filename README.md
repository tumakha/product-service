## Products API

Products REST API - test task

#### Prerequisites

Java 12+, Gradle 5 or gradle-wrapper

#### Run/Debug main class

    product.ProductApp
    
#### Build

    gradle build

#### Run web app

    cd build/libs
    java -jar product-service.jar

Note: Java 12 is required

#### HTTP Endpoints

http://localhost:8888 - Swagger REST Documentation

http://localhost:8888/v1/products - Products API

http://localhost:8888/h2 - H2 Console (Specify **jdbc:h2:mem:testdb** as JDBC URL)

http://localhost:8888/actuator/ - Actuator endpoints
