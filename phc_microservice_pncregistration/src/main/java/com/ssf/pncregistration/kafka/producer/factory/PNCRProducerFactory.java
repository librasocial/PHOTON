package com.ssf.pncregistration.kafka.producer.factory;


import com.ssf.pncregistration.kafka.producer.PNCRProducer;
import com.ssf.pncregistration.kafka.producer.exception.ProducerConfigurationException;
import com.ssf.pncregistration.kafka.producer.impl.PNCRKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Properties;


@Component
public class PNCRProducerFactory {

    static Logger logger = LoggerFactory.getLogger(PNCRProducerFactory.class);
    private PNCRProducer producer = null;

    public PNCRProducer createProducer(String bootStrapServers, String clientId, String keySerializer, String valueSerializer) throws ProducerConfigurationException {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", bootStrapServers);
        properties.setProperty("client.id", clientId);
        properties.setProperty("key.serializer", keySerializer);
        properties.setProperty("value.serializer", valueSerializer);
        logger.info("Creating new Producer ");
        producer = new PNCRKafkaProducer(properties);
        logger.info("Producer creation complete. " + producer);
        return producer;
    }
}
