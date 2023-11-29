package com.ssf.pncregistration.kafka.topics;

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
    public NewTopic createsPNCRegistrationTopic() {
        return TopicBuilder.name(env + "_" + Topics.PNCRegistration).build();
    }

    @Bean
    public NewTopic createsPNCInfantTopic() {
        return TopicBuilder.name(env + "_" + Topics.PNCInfant).partitions(1).replicas(1).build();
    }

}
