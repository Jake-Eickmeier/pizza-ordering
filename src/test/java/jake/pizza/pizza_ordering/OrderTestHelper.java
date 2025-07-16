package jake.pizza.pizza_ordering;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import jake.pizza.pizza_ordering.models.PizzaOrder;
import jake.pizza.pizza_ordering.dtos.PizzaOrderDTO;

@Component
public class OrderTestHelper {
    
    PizzaOrder getPepperoniPizzaOrder() {
        Map<String, Integer> itemsPurchased = new HashMap<>();
        itemsPurchased.put("pepperoni", 1);

        return new PizzaOrder(new ObjectId(), itemsPurchased, "pizzaLover1@gmail.com", new Date(), 1899);
    }

    PizzaOrder getPepperoniPizzaOrderWithIdAndDate(String id, Date date) {
        PizzaOrder pepperoniPizzaOrder = getPepperoniPizzaOrder();
        pepperoniPizzaOrder.setId(new ObjectId(id));
        pepperoniPizzaOrder.setPurchaseDate(date);
        return pepperoniPizzaOrder;
    }

    PizzaOrderDTO getPepperoniPizzaOrderDTO() {
        return new PizzaOrderDTO(getPepperoniPizzaOrder());
    }

}
