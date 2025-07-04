package jake.pizza.pizza_ordering.services;

import java.util.List;

import org.springframework.stereotype.Service;

import jake.pizza.pizza_ordering.dtos.PizzaOrderDTO;

@Service
public interface PizzaOrderingService {

    List<PizzaOrderDTO> findAll();

    void processPizzaOrder(PizzaOrderDTO pizzaOrder);

    boolean completePurchase(PizzaOrderDTO pizzaOrder);

    void publishPizzaPurchase(PizzaOrderDTO pizzaOrder);

    void savePizzaOrderToDatabase(PizzaOrderDTO pizzaOrder);

    void republishPizzaOrderToClient(String id);
}
