package com.kafka_producer.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartEvent {
    private UUID sessionId;
    private Integer userID;
    private Integer cartId;
    private CartEventType cartEventType;
    private Product product;
}
