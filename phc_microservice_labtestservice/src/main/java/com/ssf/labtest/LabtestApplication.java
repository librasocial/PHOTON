package com.ssf.labtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class LabtestApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabtestApplication.class, args);
    }

}
