package com.ssf.ecregistration.kafka.producer.impl;

import com.ssf.ecregistration.kafka.producer.ECRProducer;
import com.ssf.ecregistration.kafka.producer.ProducerCallback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.Future;


/**
 * @author vchocg9
 * <p>
 * Wrapper Producer class for kafka. This class will produce/publish message to the corresponding topic
 * which has been configured/passed by the client. Gives options (send methods) to publish message to
 * topic
 */
public class ECRKafkaProducer<K, V> implements ECRProducer<K, V> {

    private final Logger logger = LoggerFactory.getLogger(ECRKafkaProducer.class);

    private KafkaProducer<byte[], byte[]> producer;
    private boolean syncSend = true;
    private volatile boolean shutDown = false;

    public ECRKafkaProducer(Properties producerConfig) {
        this(producerConfig, true);
    }

    public ECRKafkaProducer(Properties producerConfig, boolean syncSend) {
        this.syncSend = syncSend;
        this.producer = new KafkaProducer<>(producerConfig);
        logger.info("Started Producer.  sync  : {}", syncSend);
    }

    @Override
    public void send(String topic, Object v) {
        send(topic, -1, null, v, new DefaultCallbackImpl());
    }

    @Override
    public void send(String topic, Object k, Object v) {
        send(topic, -1, k, v, new DefaultCallbackImpl());

    }

    @Override
    public void send(String topic, int partition, Object v) {
        send(topic, partition, null, v, new DefaultCallbackImpl());

    }

    @Override
    public void send(String topic, int partition, Object k, Object v) {
        send(topic, partition, k, v, new DefaultCallbackImpl());

    }

    @Override
    public void send(String topic, int partition, Object key, Object value, ProducerCallback callback) {
        if (shutDown) {
            throw new RuntimeException("Producer is closed.");
        }
        try {
            ProducerRecord record;
            if (partition < 0) {
                record = new ProducerRecord<>(topic, key, value);
            } else {
                record = new ProducerRecord<>(topic, partition, key, value);
            }

            Future<RecordMetadata> future = producer.send(record);
            if (!syncSend)
                return;

            future.get();
            logger.info("Message Published Successfully..");
        } catch (Exception e) {
            logger.error("Error while producing event/message to topic : {}", topic, e);
        }
    }

    public void close() {
        shutDown = true;
        try {
            producer.close();
        } catch (Exception e) {
            logger.error("Exception occurred while stopping the producer", e);
        }
    }

    @Override
    public void send(String topic, K k, V v, ProducerCallback callback) {
        send(topic, -1, k, v, callback);

    }

    private class DefaultCallbackImpl implements ProducerCallback {
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
                logger.error("Error while producing message to topic : {}", recordMetadata.topic(), e);
            } else
				/*logger.debug("sent message to topic:{} partition:{}  offset:{}", recordMetadata.topic(),
						recordMetadata.partition(), recordMetadata.offset());*/
                logger.debug("sent message to topic:{} partition:{}  offset:{}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
        }
    }
}
