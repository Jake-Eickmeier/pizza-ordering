package jake.pizza.pizza_ordering.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jake.pizza.pizza_ordering.dtos.PizzaOrderDTO;
import jake.pizza.pizza_ordering.services.PizzaOrderingService;


@RestController
public class OrderController {

    @Autowired
    PizzaOrderingService pizzaOrderingService;

    OrderController() {
    };

    @GetMapping("/hello")
    public String getHello() {
        return new String("Hello ");
    }

    @GetMapping("/test/mongoDb")
    public List<PizzaOrderDTO> getAllPizzaOrders() {
        return pizzaOrderingService.findAll();
    }

    @PostMapping("/order")
    public ResponseEntity<PizzaOrderDTO> processPizzaOrder(@RequestBody PizzaOrderDTO pizzaOrderDTO) {
        pizzaOrderingService.processPizzaOrder(pizzaOrderDTO);
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    

}
