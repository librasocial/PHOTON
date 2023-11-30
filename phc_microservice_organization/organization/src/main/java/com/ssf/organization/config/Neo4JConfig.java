//package com.ssf.organization.config;
//
//
//import com.ssf.organization.utils.SecretsConstant;
//import org.neo4j.driver.AuthTokens;
//import org.neo4j.driver.Driver;
//import org.neo4j.driver.GraphDatabase;
//import org.neo4j.ogm.session.SessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.data.neo4j.core.DatabaseSelection;
//import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
//import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
//import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@Configuration
//@EnableTransactionManagement
//@EnableNeo4jRepositories(basePackages = "com.ssf.organization")
//public class Neo4JConfig {
//
////        @Bean
////    DatabaseSelectionProvider databaseSelectionProvider(@Value("${spring.data.neo4j.database}") String database) {
////        return () -> {
////            String neo4jVersion = "3.5";
////            if (neo4jVersion == null || neo4jVersion.startsWith("4")) {
////                return DatabaseSelection.byName(database);
////            }
////            return DatabaseSelection.undecided();
////        };
////    }
//
////    @Autowired
////    private ApplicationSecretConfig applicationSecretConfig;
////
////    @Bean(name = "getSessionFactory")
////    @DependsOn({"secrets"})
////    public SessionFactory graphSessionFactory() {
////        return new SessionFactory(configuration(), "com.ssf.organization");
////    }
////
////    @Bean(name = "transactionManager")
////    @DependsOn({"secrets"})
////    public Neo4jTransactionManager graphTransactionManager() {
////        return new Neo4jTransactionManager(driver());
////    }
////
////    @Bean
////    @DependsOn({"secrets"})
////    public Driver driver() {
////        return GraphDatabase.driver(applicationSecretConfig.getSecretKey(SecretsConstant.DB_NEO4J_URI),
////                AuthTokens.basic(applicationSecretConfig.getSecretKey(SecretsConstant.DB_NEO4J_USERNAME), applicationSecretConfig.getSecretKey(SecretsConstant.DB_NEO4J_PASSWORD)));
////    }
//////
////    @Bean
////    @DependsOn({"secrets"})
////    public org.neo4j.ogm.config.Configuration configuration() {
////        org.neo4j.ogm.config.Configuration configuration =
////                new org.neo4j.ogm.config.Configuration.Builder()
////                        .connectionLivenessCheckTimeout(60000)
////                        .connectionPoolSize(10)
////                        .credentials(applicationSecretConfig.getSecretKey(SecretsConstant.DB_NEO4J_USERNAME), applicationSecretConfig.getSecretKey(SecretsConstant.DB_NEO4J_PASSWORD))
////                        .uri(applicationSecretConfig.getSecretKey(SecretsConstant.DB_NEO4J_URI)).build();
////        return configuration;
////    }
////
//}
