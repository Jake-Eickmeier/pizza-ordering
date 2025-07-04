package jake.pizza.pizza_ordering.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jake.pizza.pizza_ordering.dtos.PizzaOrderDTO;
import jake.pizza.pizza_ordering.messaging.OrderPublishingService;
import jake.pizza.pizza_ordering.repositories.PizzaOrderRepository;
import jake.pizza.pizza_ordering.models.PizzaOrder;

@Service
public class PizzaOrderingServiceImpl implements PizzaOrderingService {

    private final PizzaOrderRepository pizzaOrderRepository;
    private final OrderPublishingService orderPublishingService;

    @Value("${pizza.orders.topic}")
    private String topicName;

    public PizzaOrderingServiceImpl(PizzaOrderRepository pizzaOrderRepository, OrderPublishingService orderPublishingService) {
        this.pizzaOrderRepository = pizzaOrderRepository;
        this.orderPublishingService = orderPublishingService;
    }

    @Override
    public List<PizzaOrderDTO> findAll() {
        return pizzaOrderRepository.findAll()
                .stream()
                .map(PizzaOrderDTO::new)
                .toList();
    }

    @Override
    public boolean completePurchase(PizzaOrderDTO pizzaOrder) {
        // TODO: Implement payment processing to third-party e.g. Adyen
        return true;
    }

    @Override
    public void publishPizzaPurchase(PizzaOrderDTO pizzaOrder) {
        orderPublishingService.sendMessage(topicName, pizzaOrder.toString());
    }

    @Override
    public void processPizzaOrder(PizzaOrderDTO pizzaOrder) {
        if (completePurchase(pizzaOrder)) {
            publishPizzaPurchase(pizzaOrder);
            savePizzaOrderToDatabase(pizzaOrder);
        }
    }

    @Override
    public void savePizzaOrderToDatabase(PizzaOrderDTO pizzaOrder) {
        pizzaOrderRepository.save(pizzaOrder.toPizzaOrder());
    }

    @Override
    public void republishPizzaOrderToClient(String id) {
        PizzaOrder pizzaOrder = pizzaOrderRepository.findOne(id);
        publishPizzaPurchase(new PizzaOrderDTO(pizzaOrder));
    }

    
}
