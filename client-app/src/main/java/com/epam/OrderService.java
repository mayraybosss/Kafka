package com.epam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void sendOrderEventToOrderTopic(OrderEvent orderEvent) {
        kafkaTemplate.send("Order", orderEvent.getCorrelationId(), orderEvent);
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findByCorrelationId(orderId);
    }
}
