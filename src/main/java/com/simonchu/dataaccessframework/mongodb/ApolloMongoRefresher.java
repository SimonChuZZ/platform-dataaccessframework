package com.simonchu.dataaccessframework.mongodb;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.stereotype.Component;

/**
 * mongodb 刷新
 *
 * @author zhaozan.chu
 * @date   2018/11/14 16:05
 *
 */
@Component
public class ApolloMongoRefresher {
    private final Logger logger = LoggerFactory.getLogger(ApolloMongoRefresher.class);

    private final String host ="mongodb.host";
    private final String port ="mongodb.port";
    private final String userName ="mongodb.username";
    private final String password ="mongodb.password";
    private final String database ="mongodb.database";
    private final String connectionsPerHost ="mongodb.connectionsPerHost";
    private final String threadsAllowedToBlockForConnectionMultiplier ="mongodb.threadsAllowedToBlockForConnectionMultiplier";
    private final String maxWaitTime ="mongodb.maxWaitTime";
    private final String connectTimeout ="mongodb.connectTimeout";

    @Autowired
    private MongoProperties mongoProperties;

    @Autowired
    private MongoManager mongoManager;

    @Autowired
    private DynamicMongoTemplate dynamicMongoTemplate;

    @ApolloConfigChangeListener
    private void changeHanlder(ConfigChangeEvent changeEvent) {
        boolean isMongoChanged = false;
        String oldHost = mongoProperties.getHost();
        for (String key : changeEvent.changedKeys()) {
            if (key.startsWith("mongodb.")) {
                isMongoChanged = true;
            }

            if (host.equals(key)) {
                mongoProperties.setHost(changeEvent.getChange(key).getNewValue());
            } else if (port.equals(key)) {
                Integer tempPort = Integer.parseInt(changeEvent.getChange(key).getNewValue());
                mongoProperties.setPort(tempPort);
            } else if(userName.equals(key)){
                mongoProperties.setUserName(changeEvent.getChange(key).getNewValue());
            }else if(password.equals(key)){
                mongoProperties.setPassword(changeEvent.getChange(key).getNewValue());
            }else if(database.equals(key)){
                mongoProperties.setDatabase(changeEvent.getChange(key).getNewValue());
            }else if(connectionsPerHost.equals(key)){
                Integer temp = Integer.parseInt(changeEvent.getChange(key).getNewValue());
                mongoProperties.setConnectionsPerHost(temp);
            }else if(threadsAllowedToBlockForConnectionMultiplier.equals(key)){
                Integer temp = Integer.parseInt(changeEvent.getChange(key).getNewValue());
                mongoProperties.setThreadsAllowedToBlockForConnectionMultiplier(temp);
            }else if(maxWaitTime.equals(key)){
                Integer temp = Integer.parseInt(changeEvent.getChange(key).getNewValue());
                mongoProperties.setMaxWaitTime(temp);
            }else if(connectTimeout.equals(key)){
                Integer temp = Integer.parseInt(changeEvent.getChange(key).getNewValue());
                mongoProperties.setConnectTimeout(temp);
            }
        }

        if(isMongoChanged){
            logger.info("开始切换mongodb数据源,老数据源："+ oldHost +",新数据源："+ mongoProperties.getHost());
            MongoClient mongoClient =  mongoManager.buildMongoClient();
            MongoTemplate mongoTemplate = mongoManager.buildMongoTemplate(mongoClient);

            //更新mongodb 连接源
            if(dynamicMongoTemplate == null){
                dynamicMongoTemplate = new DynamicMongoTemplate(mongoTemplate);
            }else{
                try {
                    SimpleMongoDbFactory factory = (SimpleMongoDbFactory) dynamicMongoTemplate.getMongoTemplate().getMongoDbFactory();
                    //关闭原来连接
                    factory.destroy();
                    dynamicMongoTemplate.removeMongoTemplate();
                    // 切换mongodb
                    dynamicMongoTemplate.setMongoTemplate(mongoTemplate);
                    logger.info("成功切换mongodb数据源:"+ mongoProperties.getHost());
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(),e);
                }
            }

        }
    }
}
