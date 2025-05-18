package com.kafka_producer.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafka_producer.demo.model.CartEvent;
import com.kafka_producer.demo.producer.CartEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class CartEventController {

    @Autowired
    CartEventProducer cartEventProducer;

    //POST handler
    @PostMapping("/v1/cartEvent")
    public ResponseEntity<?> postLibraryEvent(@RequestBody CartEvent cartEvent) throws JsonProcessingException {

        //invoke kafka producer
        cartEventProducer.sendCartEvent_ProducerRecord(cartEvent);
        //libraryEventProducer.sendLibraryEvent(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartEvent);
    }

    //PUT Handler
    @PutMapping("/v1/cartEvent")
    public ResponseEntity<?> putLibraryEvent(@RequestBody CartEvent cartEvent) throws JsonProcessingException {

        ResponseEntity<String> BAD_REQUEST = validateLibraryEvent(cartEvent);
        if (BAD_REQUEST != null) return BAD_REQUEST;

        cartEventProducer.sendCartEvent_ProducerRecord(cartEvent);
        log.info("after produce call");
        return ResponseEntity.status(HttpStatus.OK).body(cartEvent);
    }

    private static ResponseEntity<String> validateLibraryEvent(CartEvent cartEvent) {
        if (cartEvent.getCartId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the LibraryEventId");
        }

        return null;
    }


}
