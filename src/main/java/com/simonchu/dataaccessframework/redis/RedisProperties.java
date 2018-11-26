package com.simonchu.dataaccessframework.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Redis 配置
 *
 * @author zhaozan.chu
 * @date   2018/11/5 15:55
 *
 */
@Configuration
public class RedisProperties {
    @Value("${spring.redis.host:null}")
    private String host;

    @Value("${spring.redis.port:0}")
    private int port;

    @Value("${spring.redis.password:test}")
    private String password;

    @Value("${spring.redis.timeout:60000}")
    private int timeout;

    @Value("${spring.redis.database:0}")
    private int database;

    @Value("${spring.redis.jedis.pool.max-active:100}")
    private int maxActive;

    @Value("${spring.redis.jedis.pool.max-wait:1000}")
    private int maxWait;

    @Value("${spring.redis.jedis.pool.max-idle:60}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle:10}")
    private int minIdle;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    @Override
    public String toString() {
        return "RedisProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", password='" + password + '\'' +
                ", timeout=" + timeout +
                ", database=" + database +
                ", maxActive=" + maxActive +
                ", maxWait=" + maxWait +
                ", maxIdle=" + maxIdle +
                ", minIdle=" + minIdle +
                '}';
    }
}
