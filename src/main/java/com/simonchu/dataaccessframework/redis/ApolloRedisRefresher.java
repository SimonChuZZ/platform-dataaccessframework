package com.simonchu.dataaccessframework.redis;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis 刷新
 *
 * @author zhaozan.chu
 * @date   2018/11/5 18:21
 *
 */
@Component
public class ApolloRedisRefresher {
    private Logger logger = LoggerFactory.getLogger(ApolloRedisRefresher.class);

    @Autowired
    private RedisProperties redisProperties;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private RedisTemplate<String,Object> objectRedisTemplate;

    @Autowired
    private RedisTemplate<String,String> stringRedisTemplate;

    private final String database ="spring.redis.database";
    private final String host ="spring.redis.host";
    private final String port ="spring.redis.port";
    private final String password ="spring.redis.password";
    private final String timeout ="spring.redis.timeout";
    private final String maxActive ="spring.redis.jedis.pool.max-active";
    private final String maxWait ="spring.redis.jedis.pool.max-wait";
    private final String maxIdle ="spring.redis.jedis.pool.max-idle";
    private final String minIdle ="spring.redis.jedis.pool.min-idle";

    @ApolloConfigChangeListener
    private void changeHanlder(ConfigChangeEvent changeEvent) {
        boolean isRedisChanged = false;
        for (String key : changeEvent.changedKeys()) {
            if (key.startsWith("spring.redis.")) {
                isRedisChanged = true;
            }

            if(database.equals(key)){
                int index =0;
                if(!StringUtils.isEmpty(changeEvent.getChange(key).getNewValue())){
                    index = Integer.parseInt(changeEvent.getChange(key).getNewValue());
                }
                redisProperties.setDatabase(index);
            }else if(host.equals(key)){
                redisProperties.setHost(changeEvent.getChange(key).getNewValue());
            }else if (port.equals(key)){
                int redisPort = 6379;
                if(!StringUtils.isEmpty(changeEvent.getChange(key).getNewValue())){
                    redisPort = Integer.parseInt(changeEvent.getChange(key).getNewValue());
                }
                redisProperties.setPort(redisPort);
            }else if(password.equals(key)){
                redisProperties.setPassword(changeEvent.getChange(key).getNewValue());
            }else if(timeout.equals(key)){
                int time = 100000;
                if(!StringUtils.isEmpty(changeEvent.getChange(key).getNewValue())){
                    time = Integer.parseInt(changeEvent.getChange(key).getNewValue());
                }
                redisProperties.setTimeout(time);
            }else if(maxActive.equals(key)){
                int max = 200;
                if(!StringUtils.isEmpty(changeEvent.getChange(key).getNewValue())){
                    max = Integer.parseInt(changeEvent.getChange(key).getNewValue());
                }
                redisProperties.setMaxActive(max);
            }else if(maxWait.equals(key)){
                int max = 1000;
                if(!StringUtils.isEmpty(changeEvent.getChange(key).getNewValue())){
                    max = Integer.parseInt(changeEvent.getChange(key).getNewValue());
                }
                redisProperties.setMaxWait(max);
            }else if(maxIdle.equals(key)){
                int max = 10;
                if(!StringUtils.isEmpty(changeEvent.getChange(key).getNewValue())){
                    max = Integer.parseInt(changeEvent.getChange(key).getNewValue());
                }
                redisProperties.setMaxIdle(max);
            }else if(minIdle.equals(key)){
                int min = 10;
                if(!StringUtils.isEmpty(changeEvent.getChange(key).getNewValue())){
                    min = Integer.parseInt(changeEvent.getChange(key).getNewValue());
                }
                redisProperties.setMinIdle(min);
            }
        }

        if(isRedisChanged){
            try {
                logger.info("开始切换Redis 数据源");
                //刷新数据源
                JedisPoolConfig jedisPoolConfig = redisManager.buildRedisConfig();
                JedisConnectionFactory jedisConnectionFactory = redisManager.buildJedisConnectionFactory(jedisPoolConfig);

                // 关闭原来的连接
                objectRedisTemplate.getConnectionFactory().getConnection().close();
                stringRedisTemplate.getConnectionFactory().getConnection().close();

                // 切换到新连接
                objectRedisTemplate.setConnectionFactory(jedisConnectionFactory);
                stringRedisTemplate.setConnectionFactory(jedisConnectionFactory);

                logger.info("成功切换到新的数据源:"+ redisProperties.getHost());
            }catch (Exception ex){
                logger.error("切换数据源出错："+ ex.getMessage());
            }
        }
    }
}
