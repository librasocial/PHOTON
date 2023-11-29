package com.ssf.membership.kafka.producer.factory;

import com.ssf.membership.kafka.producer.MembershipProducer;
import com.ssf.membership.kafka.producer.exception.ProducerConfigurationException;
import com.ssf.membership.kafka.producer.impl.MembershipKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Properties;


@Component
public class MembershipProducerFactory {

    static Logger logger = LoggerFactory.getLogger(MembershipProducerFactory.class);
    private MembershipProducer producer = null;

    public MembershipProducer createProducer(String bootStrapServers, String clientId, String keySerializer, String valueSerializer) throws ProducerConfigurationException {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", bootStrapServers);
        properties.setProperty("client.id", clientId);
        properties.setProperty("key.serializer", keySerializer);
        properties.setProperty("value.serializer", valueSerializer);
        logger.info("Creating new Producer ");
        producer = new MembershipKafkaProducer(properties);
        logger.info("Producer creation complete. " + producer);
        return producer;
    }
}
