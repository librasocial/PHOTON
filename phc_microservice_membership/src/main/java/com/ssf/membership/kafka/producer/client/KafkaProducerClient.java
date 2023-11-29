package com.ssf.membership.kafka.producer.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.membership.config.ApplicationSecretConfig;
import com.ssf.membership.kafka.producer.MembershipProducer;
import com.ssf.membership.kafka.producer.factory.MembershipProducerFactory;
import com.ssf.membership.kafka.producer.model.KafkaMessage;
import com.ssf.membership.utils.SecretsConstant;
import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class KafkaProducerClient {

    private final static Logger log = LoggerFactory.getLogger(KafkaProducerClient.class);
    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();

    @Autowired
    private ApplicationSecretConfig applicationSecretConfig;

    @Value("${spring.kafka.client-id}")
    private String clientId;
    @Autowired
    private MembershipProducerFactory factory;
    @Value("${spring.env}")
    private String env;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, applicationSecretConfig.getSecretKey(SecretsConstant.KAFKA_BROKERS));
        configs.put(AdminClientConfig.CLIENT_ID_CONFIG, clientId);
        return new KafkaAdmin(configs);
    }

    @SneakyThrows
    public void publishToTopic(String topic, KafkaMessage kafkaMessage) {

        log.info("Initilizing the producer properties: bootstrap-server:{}, client-id:{}", applicationSecretConfig.getSecretKey(SecretsConstant.KAFKA_BROKERS), clientId);
        MembershipProducer producer = factory.createProducer(applicationSecretConfig.getSecretKey(SecretsConstant.KAFKA_BROKERS), clientId, "org.apache.kafka.common.serialization.StringSerializer", "org.springframework.kafka.support.serializer.JsonSerializer");
        log.info("Initilized the producer properties");
        log.info("Initialized the kafka message to be added to the topic");
        kafkaMessage.setInstrument(clientId);
        if (producer != null) {
            log.info("Publishing the message to Kafka.");
            producer.send(env + "_" + topic, kafkaMessage.getGuid(), kafkaMessage);
            log.info("Published Successfully");
        } else {
            log.error("Producer is {}", producer);
        }
        producer.close();
    }
}
