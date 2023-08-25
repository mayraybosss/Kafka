package com.epam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CourierService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    @KafkaListener(topics = "Notification", groupId = "${spring.kafka.consumer.group-id}")
    public void processNotification(Order order) {
        if ("pizza is ready".equals(order.getStatus())) {
            order.setStatus("picked up by courier");
            orderRepository.save(order);
            sendCourierEventToNotificationTopic(order);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            order.setStatus("Pizza delivered");
            orderRepository.save(order);
            sendCourierEventToNotificationTopic(order);
        }
    }

    public void sendCourierEventToNotificationTopic(Order order) {
        kafkaTemplate.send("Notification", order.getCorrelationId(), order);
    }
}

