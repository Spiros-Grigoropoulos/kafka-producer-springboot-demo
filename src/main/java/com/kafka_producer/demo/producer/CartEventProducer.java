package com.kafka_producer.demo.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka_producer.demo.model.CartEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class CartEventProducer {

    @Value("${spring.kafka.topic}")
    public String topic;

    @Autowired
    private KafkaTemplate<Integer, Object> kafkaTemplate;

    public CompletableFuture<SendResult<Integer, Object>> sendCartEvent_Async(CartEvent cartEvent) throws JsonProcessingException {

        // Set the CartId as the key for the message
        var key = cartEvent.getCartId();

        // Publish the message on Kafka
        var completableFuture = kafkaTemplate.send(topic, key, cartEvent);

        // Check the publishing status
        return completableFuture.whenComplete(((sendResult, throwable) -> {
            if (throwable != null) {
                handleFailure(key, cartEvent, throwable);
            } else {
                handleSuccess(key, cartEvent, sendResult);
            }
        }));
    }

    public CompletableFuture<SendResult<Integer, Object>> sendCartEvent_ProducerRecord(CartEvent cartEvent) throws JsonProcessingException {

        // Set the CartId as the key for the message and  serialize the message
        var key = cartEvent.getCartId();
        var producerRecord = buildProducerRecord(key, cartEvent);

        // Publish the message on Kafka
        var completableFuture = kafkaTemplate.send(producerRecord);

        // Check the publishing status
        return completableFuture.whenComplete(((sendResult, throwable) -> {
            if (throwable != null) {
                handleFailure(key, cartEvent, throwable);
            } else {
                handleSuccess(key, cartEvent, sendResult);
            }
        }));
    }

    private ProducerRecord<Integer, Object> buildProducerRecord(Integer key, Object value) {
        List<Header> recordHeader = List.of(new RecordHeader("event-source", "library-event-producer".getBytes()));
        return new ProducerRecord<>(topic, null, key, value, recordHeader);
    }

    private void handleSuccess(Integer key, Object value, SendResult<Integer, Object> sendResult) {
        log.info("Message sent successfully for the key: {} and the value: {}, partition is: {}",
                key, value, sendResult.getRecordMetadata().partition());
    }

    private void handleFailure(Integer key, Object value, Throwable throwable) {
        log.error("Error sending message and exception is {}", throwable.getMessage(), throwable);
    }
}
