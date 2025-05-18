package com.kafka_producer.demo.producer;

import com.kafka_producer.demo.model.CartEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RoutingKafkaProducer {

    private RoutingKafkaTemplate routingTemplate;

    public void sendDefaultTopic(String message) {
        routingTemplate.send("default-topic", message.getBytes());
    }

    public void sendInventoryEvent(CartEvent cartEvent) {

        routingTemplate.send("cart-events", cartEvent);
    }
}
