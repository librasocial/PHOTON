package com.ssf.idspsurveillance.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.google.gson.Gson;
import com.ssf.idspsurveillance.constant.SecretsConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class ApplicationSecretConfig {
    Gson gson = new Gson();

    @Autowired
    private Environment env;

    @Value("${spring.kafka.bootstrap-servers}")
    private String localBootstrapServers;

    @Value("${spring.env}")
    private String environment;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.secret.name}")
    private String secretName;

    private Map<String, String> secretKeys = new HashMap<>();

    @SneakyThrows
    private Map<String, String> getSecretFromAWS() {

        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(env.getProperty("aws.access.key"), env.getProperty("aws.secret.key"))))
                .build();


        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult;

        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        if (getSecretValueResult.getSecretString() != null) {
            String secret = getSecretValueResult.getSecretString();
            Map<String, String> secretKeys = gson.fromJson(secret, Map.class);
            return secretKeys;
        } else {
            return null;
        }
    }

    @Bean("secrets")
    void setSecretKeys() {
        log.info("Started with Profile : {}", environment);
        this.secretKeys = getSecretFromAWS();
        if (environment.equalsIgnoreCase("local") || environment.equalsIgnoreCase("test")) {
            this.secretKeys.put(SecretsConstant.KAFKA_BROKERS, localBootstrapServers);
        }
    }

    public String getSecretKey(String key) {
        return this.secretKeys.get(key);
    }
}
