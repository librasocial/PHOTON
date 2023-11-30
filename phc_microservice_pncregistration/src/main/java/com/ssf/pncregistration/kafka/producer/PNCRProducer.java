package com.ssf.pncregistration.kafka.producer;

public interface PNCRProducer<K, V> {

	/**
	 * @param topic
	 * @param v message
	 */
	public void send(String topic, V v);

	/**
	 * @param topic
	 * @param k key
	 * @param v value
	 */
	public void send(String topic, K k, V v);

	/**
	 * @param topic
	 * @param partition
	 * @param v value
	 */
	public void send(String topic, int partition, V v);

	/**
	 * @param topic
	 * @param partition
	 * @param k key
	 * @param v value
	 */
	public void send(String topic, int partition, K k, V v);

	/**
	 * @param topic
	 * @param partition
	 * @param key
	 * @param value
	 * @param callback
	 */
	public void send(String topic, int partition, K key, V value, ProducerCallback callback);

	/**
	 * @param topic
	 * @param k key
	 * @param v value
	 * @param callback
	 */
	public void send(String topic, K k, V v, ProducerCallback callback);

	/**
	 * closes the producer
	 */
	public void close();
}
