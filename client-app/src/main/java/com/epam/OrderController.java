package com.epam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private static int counter = 1;

    @GetMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderEvent orderEvent) {
        Order order = new Order();
        order.setStatus("I want to buy a pizza");
        order.setCorrelationId(String.valueOf(counter++));
        orderService.saveOrder(order);
        orderService.sendOrderEventToOrderTopic(orderEvent);

        return ResponseEntity.ok("Order received and sent for processing");
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        Order order = orderService.getOrderById(orderId);

        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

