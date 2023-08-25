package com.epam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    public void sendOrderStatusToNotificationTopic(Order order) {
        kafkaTemplate.send("Notification", order.getCorrelationId(), order);
    }

    @KafkaListener(topics = "Order", groupId = "${spring.kafka.consumer.group-id}")
    public void processOrder(OrderEvent orderEvent) {
        Order order = orderRepository.findByCorrelationId(orderEvent.getCorrelationId());

        if (order != null) {
            order.setStatus("start cooking");
            orderRepository.save(order);
            sendOrderStatusToNotificationTopic(order);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            order.setStatus("pizza is ready");
            orderRepository.save(order);

            sendOrderStatusToNotificationTopic(order);
        }
    }
}
