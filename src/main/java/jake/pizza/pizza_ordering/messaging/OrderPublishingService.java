package jake.pizza.pizza_ordering.messaging;

import org.springframework.stereotype.Service;

@Service
public interface OrderPublishingService {
    public void sendMessage(String topic, String message);
}
