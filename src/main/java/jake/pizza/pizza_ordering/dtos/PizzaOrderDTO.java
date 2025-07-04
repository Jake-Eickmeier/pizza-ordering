package jake.pizza.pizza_ordering.dtos;

import java.util.Date;
import java.util.Map;

import org.bson.types.ObjectId;

import jake.pizza.pizza_ordering.models.PizzaOrder;

public record PizzaOrderDTO(
        String id,
        Map<String, Integer> itemsPurchased,
        String userEmailAddress,
        Date purchaseDate,
        int priceInCents) {

    public PizzaOrderDTO(PizzaOrder pizzaOrder) {
        this(pizzaOrder.getId().toHexString(), pizzaOrder.getItemsPurchased(), pizzaOrder.getUserEmailAddress(), pizzaOrder.getPurchaseDate(), pizzaOrder.getPriceInCents());
    }

    public PizzaOrder toPizzaOrder() {
        ObjectId objectId = id == null ? new ObjectId() : new ObjectId(id);
        return new PizzaOrder(objectId, itemsPurchased, userEmailAddress, purchaseDate, priceInCents);
    }

}
