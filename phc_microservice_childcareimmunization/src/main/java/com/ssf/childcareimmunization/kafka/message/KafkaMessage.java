package com.ssf.childcareimmunization.kafka.message;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class KafkaMessage<T> {
	private Context context;

	private String type;

	private String guid = UUID.randomUUID().toString();

	private Actor actor;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private LocalDateTime eventTime = LocalDateTime.now();

	private String instrument;

	private T object;

}
