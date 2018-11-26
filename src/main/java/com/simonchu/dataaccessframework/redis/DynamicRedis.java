package com.simonchu.dataaccessframework.redis;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 动态Redis
 *
 * @author zhaozan.chu
 * @date   2018/11/5 16:02
 *
 */
public class DynamicRedis {
    private final AtomicReference<RedisTemplate<String, Object>> objectRedisTemplateAtomicReference ;
    private final AtomicReference<RedisTemplate<String,String>> stringRedisTemplateAtomicReference ;

    public DynamicRedis(RedisTemplate<String, Object> objectRedisTemplate,
                        RedisTemplate<String,String> stringStringRedisTemplate){
        objectRedisTemplateAtomicReference = new AtomicReference<>(objectRedisTemplate);
        stringRedisTemplateAtomicReference = new AtomicReference<>(stringStringRedisTemplate);
    }

    public RedisTemplate<String, Object> setObjectRedisTemplate(RedisTemplate<String, Object> redisTemplate){
        return objectRedisTemplateAtomicReference.getAndSet(redisTemplate);
    }

    public RedisTemplate<String, Object> getObjectRedisTemplate(){
        return objectRedisTemplateAtomicReference.get();
    }

    public RedisTemplate<String, String> setStringRedisTemplate(RedisTemplate<String, String> redisTemplate){
        return stringRedisTemplateAtomicReference.getAndSet(redisTemplate);
    }

    public RedisTemplate<String, String> getStringRedisTemplate(){
        return stringRedisTemplateAtomicReference.get();
    }
}
