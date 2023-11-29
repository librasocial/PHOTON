package com.ssf.organization.kafka.producer.factory;


import com.ssf.organization.kafka.producer.OrganizationProducer;
import com.ssf.organization.kafka.producer.exception.ProducerConfigurationException;
import com.ssf.organization.kafka.producer.impl.OrganizationKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Properties;


@Component
public class OrganizationProducerFactory {

    static Logger logger = LoggerFactory.getLogger(OrganizationProducerFactory.class);
    private OrganizationProducer producer = null;

    public OrganizationProducer createProducer(String bootStrapServers, String clientId, String keySerializer, String valueSerializer) throws ProducerConfigurationException {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", bootStrapServers);
        properties.setProperty("client.id", clientId);
        properties.setProperty("key.serializer", keySerializer);
        properties.setProperty("value.serializer", valueSerializer);
        logger.info("Creating new Producer ");
        producer = new OrganizationKafkaProducer(properties);
        logger.info("Producer creation complete. " + producer);
        return producer;
    }
}
