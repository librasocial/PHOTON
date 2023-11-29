package com.ssf.ancregistration.kafka;

import com.ssf.ancregistration.kafka.message.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @Value("${spring.kafka.client-id}")
    private String clientId;

    @Value("${spring.env}")
    private String topicEnv;

    public void sendMessage(String topic, KafkaMessage kafkaMessage) {
        log.info("Sending the kafka message to the topic {} ", topic);
        kafkaMessage.setInstrument(clientId);
        kafkaTemplate.send(topicEnv + "_" + topic, kafkaMessage.getGuid(), kafkaMessage);
        log.info("Published Successfully ");
    }

}
