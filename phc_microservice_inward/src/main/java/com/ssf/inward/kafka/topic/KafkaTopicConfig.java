package com.ssf.inward.kafka.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.env}")
    private String env;

    @Bean
    public NewTopic createsANCRegistrationTopic() {
        return TopicBuilder.name(env + "_" + Topics.Inward.toString()).replicas(1).partitions(1).build();
    }


}
