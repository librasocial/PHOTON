package com.ssf.ecregistration.kafka.producer.client;

import com.ssf.ecregistration.config.ApplicationSecretConfig;
import com.ssf.ecregistration.kafka.producer.ECRProducer;
import com.ssf.ecregistration.kafka.producer.factory.ECRProducerFactory;
import com.ssf.ecregistration.kafka.producer.model.KafkaMessage;
import com.ssf.ecregistration.utils.SecretsConstant;
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
    @Value("${spring.kafka.client-id}")
    private String clientId;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Autowired
    private ECRProducerFactory factory;

    @Value("${spring.env}")
    private String env;

    @Autowired
    private ApplicationSecretConfig applicationSecretConfig;

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
        ECRProducer producer = factory.createProducer(applicationSecretConfig.getSecretKey(SecretsConstant.KAFKA_BROKERS), clientId, keySerializer, valueSerializer);
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
