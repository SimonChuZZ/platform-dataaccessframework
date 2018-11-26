package com.simonchu.dataaccessframework.redis;

import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * Redis 管理器，负责构建redis 连接对象
 *
 * @author zhaozan.chu
 * @date   2018/11/6 9:48
 *
 */
@Component
public class RedisManager {
    @Autowired
    private RedisProperties redisProperties;

    /**
     * 生成连接池配置
     * @return
     */
    public JedisPoolConfig buildRedisConfig(){
        if(null == redisProperties){
            return null;
        }

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(redisProperties.getMaxWait());
        config.setMaxIdle(redisProperties.getMaxIdle());
        config.setMinIdle(redisProperties.getMinIdle());
        config.setMaxTotal(redisProperties.getMaxActive());
        return config;
    }

    /**
     * 构建Jedis 连接工厂
     * @return
     */
    public JedisConnectionFactory buildJedisConnectionFactory(JedisPoolConfig jedisPoolConfig){
        if("null".equals(redisProperties.getHost()) || redisProperties.getPort()<=0){
            return null;
        }

        if(null == jedisPoolConfig){
            return null;
        }

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        //设置redis服务器的host，ip地址
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
        if(!StringUtils.isEmpty(redisProperties.getPassword())){
            redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        }
        JedisClientConfiguration.JedisClientConfigurationBuilder jpcf = JedisClientConfiguration.builder();
        jpcf.connectTimeout(Duration.ofMillis(redisProperties.getTimeout()));
        // 添加连接池配置
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder poolingClientConfigurationBuilder
                = jpcf.usePooling();
        poolingClientConfigurationBuilder.poolConfig(jedisPoolConfig);

        //通过构造器来构造jedis客户端配置
        JedisClientConfiguration jedisClientConfiguration = jpcf.build();
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }

    /**
     * 构建对象RedisTemplate
     * @param jedisConnectionFactory 连接工厂
     * @return
     */
    public RedisTemplate<String, Object> buildObjectRedisTemplate(JedisConnectionFactory jedisConnectionFactory){
        if(null == jedisConnectionFactory){
            return null;
        }

        RedisTemplate<String, Object> template =  new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory);
        //打开autotype 验证
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        // 设置序列化
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        // value值的序列化采用fastJsonRedisSerializer
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);
        // key的序列化采用StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // 开启事务
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 构建String RedisTemplate
     * @param jedisConnectionFactory 连接工厂
     * @return
     */
    public RedisTemplate<String, String> buildStringRedisTemplate(JedisConnectionFactory jedisConnectionFactory){
        if(null == jedisConnectionFactory){
            return null;
        }

        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);

        // 开启事务
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
