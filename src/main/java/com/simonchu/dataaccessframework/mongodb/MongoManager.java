package com.simonchu.dataaccessframework.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.simonchu.dataaccessframework.constant.CommonConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Mongodb 生成管理器
 *
 * @author zhaozan.chu
 * @date   2018/11/14 16:09
 *
 */
@Component
public class MongoManager {
    private final Logger logger = LoggerFactory.getLogger(MongoManager.class);

    @Autowired
    private MongoProperties mongoProperties;

    public MongoClient buildMongoClient() {
        try {
            if (mongoProperties.getHost() == null || CommonConstant.NULL.equals(mongoProperties.getHost())) {
                logger.info("未设置Mongodb");
                return null;
            }

            if (mongoProperties.getPort() < 1) {
                logger.error("未设置Mongodb 的 port");
                throw new IllegalArgumentException("未设置Mongodb 的 port");
            }

            //如果没有配置账号密码，则使用匿名登录
            if (mongoProperties.getUserName() == null || mongoProperties.getUserName().isEmpty()
                    || mongoProperties.getPassword() == null || mongoProperties.getPassword().isEmpty()) {
                MongoClient mongoClient = new MongoClient(mongoProperties.getHost(), mongoProperties.getPort());
                return mongoClient;
            }

            ServerAddress serverAddress = new ServerAddress(mongoProperties.getHost(), mongoProperties.getPort());
            MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
            builder.connectionsPerHost(mongoProperties.getConnectionsPerHost());
            builder.maxWaitTime(mongoProperties.getMaxWaitTime());
            builder.connectTimeout(mongoProperties.getConnectTimeout());
            builder.threadsAllowedToBlockForConnectionMultiplier(mongoProperties.getThreadsAllowedToBlockForConnectionMultiplier());
            MongoClientOptions options = builder.build();

            return new MongoClient(serverAddress,options);
        } catch (Exception ex) {
            logger.error(ex.toString());
            return null;
        }
    }

    public MongoTemplate buildMongoTemplate(MongoClient client){
        if(null == client){
            logger.error("未生成MongoDB client");
            return null;
        }

        if(StringUtils.isEmpty(mongoProperties.getDatabase())){
            logger.error("未配置mongodb.database");
            return null;
        }

        logger.info("初始化 mongoTemplate");
        SimpleMongoDbFactory dbFactory = new SimpleMongoDbFactory(client, mongoProperties.getDatabase());
        return new MongoTemplate(dbFactory);
    }
}
