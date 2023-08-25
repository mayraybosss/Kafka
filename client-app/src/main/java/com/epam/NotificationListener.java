package com.epam;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    @KafkaListener(topics = "Notification", groupId = "client-group")
    public void processOrderStatusEvent(Order orderStatusEvent) {
        System.out.println("Received order status update: " + orderStatusEvent.getStatus());
    }
}

