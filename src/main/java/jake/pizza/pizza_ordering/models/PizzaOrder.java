package jake.pizza.pizza_ordering.models;

import java.util.Date;
import java.util.Map;

import org.bson.types.ObjectId;

public class PizzaOrder {

    private ObjectId id;

    private Map<String, Integer> itemsPurchased;

    private String userEmailAddress;

    private Date purchaseDate;

    private int priceInCents;


    public PizzaOrder(ObjectId id, Map<String, Integer> itemsPurchased,
            String userEmailAddress, Date purchaseDate, int priceInCents) {
        this.id = id;
        this.itemsPurchased = itemsPurchased;
        this.userEmailAddress = userEmailAddress;
        this.purchaseDate = purchaseDate;
        this.priceInCents = priceInCents;
    }

    public PizzaOrder() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Map<String, Integer> getItemsPurchased() {
        return itemsPurchased;
    }

    public void setItemsPurchased(Map<String, Integer> itemsPurchased) {
        this.itemsPurchased = itemsPurchased;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getPriceInCents() {
        return priceInCents;
    }

    public void setPriceInCents(int priceInCents) {
        this.priceInCents = priceInCents;
    }

    @Override
    public String toString() {
        return "PizzaOrder [id=" + id + ", itemsPurchased=" + itemsPurchased + ", userEmailAddress=" + userEmailAddress
                + ", purchaseDate=" + purchaseDate + ", priceInCents=" + priceInCents + "]";
    }


}
