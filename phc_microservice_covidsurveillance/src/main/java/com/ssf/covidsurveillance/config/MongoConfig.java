package com.ssf.covidsurveillance.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.ssf.covidsurveillance.constants.SecretsConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.Collections;

@Configuration
@DependsOn({"secrets"})
@EnableMongoRepositories(basePackages = "com.ssf.covidsurveillance.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Autowired
    private ApplicationSecretConfig applicationSecretConfig;

    @Value("${spring.env}")
    private String environment;

    @Override
    protected String getDatabaseName() {
        return environment + "-covidsurveillance";
    }

    @Override
    public MongoClient mongoClient() {
        final ConnectionString connectionString = new ConnectionString(applicationSecretConfig.getSecretKey(SecretsConstant.DB_LINK));
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .retryWrites(true)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Override
    public Collection<String> getMappingBasePackages() {
        return Collections.singleton("com.ssf.covidsurveillance.repository");
    }
}