package com.simonchu.dataaccessframework.mongodb;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@AutoConfigureAfter({MongoConfiguration.class,MongoManager.class})
public class MongoConfiguration {
    @Autowired
    private MongoManager mongoManager;

    @Bean
    public DynamicMongoTemplate dynamicMongoTemplate(){
        MongoClient mongoClient =  mongoManager.buildMongoClient();
        MongoTemplate mongoTemplate = mongoManager.buildMongoTemplate(mongoClient);
        return new DynamicMongoTemplate(mongoTemplate);
    }
}
