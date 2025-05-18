package com.kafka_producer.demo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AutoCreateTopicConfig {

    @Value("${spring.kafka.topic}")
    public String topic;

    @Value("${topic.partitions}")
    public Integer partition;

    @Value("${topic.replicas}")
    public Integer replicas;

    @Bean
    public NewTopic inventoryEvents() {
        return TopicBuilder.name(topic)
                .partitions(partition)
                .replicas(replicas)
                .build();
    }

    @Bean
    public NewTopic cartEvents() {
        return TopicBuilder.name(topic)
                .partitions(partition)
                .replicas(replicas)
                .build();
    }
}
