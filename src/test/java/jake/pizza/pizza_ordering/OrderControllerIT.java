package jake.pizza.pizza_ordering;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import jakarta.annotation.PostConstruct;
import jake.pizza.pizza_ordering.models.PizzaOrder;
import jake.pizza.pizza_ordering.dtos.PizzaOrderDTO;
import jake.pizza.pizza_ordering.repositories.PizzaOrderRepository;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.time.Duration;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private PizzaOrderRepository pizzaOrderRepository;

    @Autowired
    private OrderTestHelper orderTestHelper;

    private String serviceURL;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Container
    static ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.8.0")
        .withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "true");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        kafkaContainer.start();
        mongoDBContainer.waitingFor(Wait.forListeningPort()
           .withStartupTimeout(Duration.ofSeconds(20L)));
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.kafka.producer.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @Autowired
    OrderControllerIT(MongoClient mongoClient) {
        createPizzaCollectionIfNotPresent(mongoClient);
    }

    @PostConstruct
    void setUp() {
        serviceURL = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        pizzaOrderRepository.deleteAll();
    }

    @AfterAll
    static void stopContainers() {
        mongoDBContainer.stop();
        kafkaContainer.close();
        kafkaContainer.stop();
    }

    @DisplayName("GET /hello")
    @Test
    void getHello() {
        ResponseEntity<String> result = rest.exchange(serviceURL + "/hello", HttpMethod.GET, null,
                                                               new ParameterizedTypeReference<>() {
                                                               });
        assertThat(result.getStatusCode().is2xxSuccessful());
    }

    @DisplayName("POST /order")
    @Test
    void postPizzaOrder() {
        // GIVEN
        PizzaOrderDTO pizzaOrderDTO = orderTestHelper.getPepperoniPizzaOrderDTO();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PizzaOrderDTO> requestEntity = new HttpEntity<>(pizzaOrderDTO, headers);

        // WHEN
        ResponseEntity<PizzaOrderDTO> result = rest.exchange(serviceURL + "/order", HttpMethod.POST, requestEntity,
                                                        new ParameterizedTypeReference<>() {
                                                        });

        // THEN
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        List<PizzaOrder> savedPizzaOrders = pizzaOrderRepository.findAll();
        PizzaOrder expected = orderTestHelper.getPepperoniPizzaOrderWithIdAndDate(pizzaOrderDTO.id(), pizzaOrderDTO.purchaseDate());
        assertThat(savedPizzaOrders).singleElement().usingRecursiveComparison().isEqualTo(expected);
    }

    private void createPizzaCollectionIfNotPresent(MongoClient mongoClient) {
        // This is required because it is not possible to create a new collection within a multi-documents transaction.
        // Some tests start by inserting 2 documents with a transaction.
        MongoDatabase db = mongoClient.getDatabase("test");
        if (!db.listCollectionNames().into(new ArrayList<>()).contains("pizzaOrders")) {
            db.createCollection("pizzaOrders");
        }
    }
}
