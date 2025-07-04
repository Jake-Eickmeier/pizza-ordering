package jake.pizza.pizza_ordering.messaging;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class OrderPublishingServiceImpl implements OrderPublishingService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String topicName, String message) {
        // kafkaTemplate.send(topic, message);
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + message + 
                "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" + 
                    message + "] due to : " + ex.getMessage());
            }
        });
        }
    
}
