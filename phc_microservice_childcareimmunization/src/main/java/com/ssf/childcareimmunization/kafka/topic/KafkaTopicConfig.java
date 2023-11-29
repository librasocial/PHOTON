package com.ssf.childcareimmunization.kafka.topic;

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
	public NewTopic createsECRegistrationTopic() {
		return TopicBuilder.name(env + Topics.ChildCareImmunization).build();
	}
}
