package com.ssf.ecregistration.kafka.producer.factory;

import com.ssf.ecregistration.kafka.producer.ECRProducer;
import com.ssf.ecregistration.kafka.producer.exception.ProducerConfigurationException;
import com.ssf.ecregistration.kafka.producer.impl.ECRKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Properties;


@Component
public class ECRProducerFactory {

    static Logger logger = LoggerFactory.getLogger(ECRProducerFactory.class);
    private ECRProducer producer = null;

    public ECRProducer createProducer(String bootStrapServers, String clientId, String keySerializer, String valueSerializer) throws ProducerConfigurationException {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", bootStrapServers);
        properties.setProperty("client.id", clientId);
        properties.setProperty("key.serializer", keySerializer);
        properties.setProperty("value.serializer", valueSerializer);
        logger.info("Creating new Producer ");
        producer = new ECRKafkaProducer(properties);
        logger.info("Producer creation complete. " + producer);
        return producer;
    }
}
