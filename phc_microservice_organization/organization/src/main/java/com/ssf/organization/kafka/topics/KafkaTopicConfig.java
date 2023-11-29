package com.ssf.organization.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.env}")
    private String env;

//    @Bean
    public NewTopic createsHouseHoldTopic() {
        return TopicBuilder.name(env + "_" + Topics.HouseHold).partitions(3).replicas(2).build();
    }

}
