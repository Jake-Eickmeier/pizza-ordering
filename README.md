## pizza-ordering

This service handles the processing of orders ocurring in the pizza webapp. When an order is POSTed to this service, it saves it to a local MongoDB collection and produces a message to a kafka-topic that can be read by another service that would be responsible for fulfillment at a given restaurant. In the event that a restaurant fails to handle a given message, they should be able to write it to a dead letter queue, but if a downstream service completely fails there is also a replay method wherein an order can be re-delivered to the message queue from the mongo collection. This service would also be responsible for requesting payment processing from a third-party provider such as Adyen before delivering the message, if it were real and implemented.


### How to run
Before running, make sure the required MongoDB collection and kafka topic is available. You can accomplish this by running `docker-compose up -d` in the root directory of `pizza-project`, which will run all dependencies for all services, or run the same command in the root of this project which will run only the dependencies required for this service.
You can run this service locally using your IDE or the command `mvn spring-boot:run`, which will make the service available on port 8080. Swagger documentation is available [here](http://localhost:8080/swagger-ui/index.html).