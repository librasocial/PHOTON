package com.ssf.pncservice.kafka;

import com.ssf.pncservice.config.ApplicationSecretConfig;
import com.ssf.pncservice.kafka.message.KafkaMessage;
import com.ssf.pncservice.utils.SecretsConstant;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@DependsOn({"secrets"})
public class KafkaProducerConfig {
    @Value("${spring.kafka.client-id}")
    private String clientId;
    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;
    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;
    @Autowired
    private ApplicationSecretConfig applicationSecretConfig;

    @Bean
    @DependsOn({"secrets"})
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, applicationSecretConfig.getSecretKey(SecretsConstant.KAFKA_BROKERS));
        configs.put(AdminClientConfig.CLIENT_ID_CONFIG, clientId);
        return new KafkaAdmin(configs);
    }

    @Bean
    @DependsOn({"secrets"})
    public ProducerFactory producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationSecretConfig.getSecretKey(SecretsConstant.KAFKA_BROKERS));
        config.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    @DependsOn({"secrets"})
    public KafkaTemplate<String, KafkaMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
