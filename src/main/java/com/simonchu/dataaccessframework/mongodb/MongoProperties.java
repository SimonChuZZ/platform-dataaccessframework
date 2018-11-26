package com.simonchu.dataaccessframework.mongodb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoProperties {
    @Value("${mongodb.host:null}")
    private String host;
    @Value("${mongodb.port:27017}")
    private Integer port;
    @Value("${mongodb.username:root}")
    private String userName;
    @Value("${mongodb.password:null}")
    private String password;
    @Value("${mongodb.database:null}")
    private String database;
    @Value("${mongodb.connectionsPerHost:8}")
    private Integer connectionsPerHost;
    @Value("${mongodb.threadsAllowedToBlockForConnectionMultiplier:4}")
    private Integer threadsAllowedToBlockForConnectionMultiplier;
    @Value("${mongodb.maxWaitTime:120000}")
    private Integer maxWaitTime;
    @Value("${mongodb.connectTimeout:60000}")
    private Integer connectTimeout;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Integer getConnectionsPerHost() {
        return connectionsPerHost;
    }

    public void setConnectionsPerHost(Integer connectionsPerHost) {
        this.connectionsPerHost = connectionsPerHost;
    }

    public Integer getThreadsAllowedToBlockForConnectionMultiplier() {
        return threadsAllowedToBlockForConnectionMultiplier;
    }

    public void setThreadsAllowedToBlockForConnectionMultiplier(Integer threadsAllowedToBlockForConnectionMultiplier) {
        this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }

    public Integer getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(Integer maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    @Override
    public String toString() {
        return "MongoProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", database='" + database + '\'' +
                ", connectionsPerHost=" + connectionsPerHost +
                ", threadsAllowedToBlockForConnectionMultiplier=" + threadsAllowedToBlockForConnectionMultiplier +
                ", maxWaitTime=" + maxWaitTime +
                ", connectTimeout=" + connectTimeout +
                '}';
    }
}
