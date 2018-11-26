package com.simonchu.dataaccessframework.database;

import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Redis 与 Mybatis 缓存集成
 *
 * @author zhaozan.chu
 * @date 2018/10/18 11:28
 *
 * 使用缓存注意点：
 *		1. 每个表的增删改查等操作，必须在表对应的配置文件中，使用同一个命名空间，防止因为不同命名空间下操作相同表带来缓存隐患。
 *		2.复杂的查询语句或者实时性要求高的不允许使用缓存。其他查询方法，由程序员根据实际情况，合适的选择是否缓存（useCache=“false”）。
 *		3. insert、update、delete 等操作是否刷新缓存，由程序员进行控制（不刷新缓存使用flushCache="false"）。
 *		4. 缓存策略，默认使用LRU策略，缓存刷新时间默认30分钟，缓存数据10000条。
 */
public class MybatisRedisCache implements Cache {
    // 日志
    private static final Logger logger = LoggerFactory.getLogger(MybatisRedisCache.class);

    private static RedisTemplate<String,Object> redisTemplate;// redisClient;

    // 缓存数据有效期，默认为2天
    private static final int EXPIRE_TIME_SECOND = 2*24*60*60;

    public static void setRedisClient(RedisTemplate<String,Object> redisTemplate) {
        MybatisRedisCache.redisTemplate = redisTemplate;
    }

    // 对象ID
    private final String id;

    /**
     * 读写锁
     */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public MybatisRedisCache(final String id) {
        if (id == null) {
            logger.error("MybatisRedisCache instances require an ID");
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        logger.debug("MybatisRedisCache:id=" + id);
        this.id = id;
    }

    /**
     * 获取对象ID
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * 添加对象
     */
    @Override
    public void putObject(Object key, Object value) {
        // 如果键值对中有任何一个为null时候，都不保存
        if (key == null || value == null) {
            return;
        }

        try {
            redisTemplate.opsForValue().set(key.toString(),value,EXPIRE_TIME_SECOND,TimeUnit.SECONDS);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     * 获取对象
     */
    @Override
    public Object getObject(Object key) {
        // 如果键为null，则不获取
        if (null == key) {
            return null;
        }

        try {
            logger.info("从缓存中获取数据，key:"+ key.toString());
            return redisTemplate.opsForValue().get(key.toString());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return null;
    }

    /**
     * 移除对象
     */
    @Override
    public Object removeObject(Object key) {
        if (key == null) {
            return null;
        }

        try {
            Object result = redisTemplate.opsForValue().get(key.toString());
            if(null == result){
                return null;
            }

            redisTemplate.expire(key.toString(),0, TimeUnit.SECONDS);
            return result;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    /**
     * 清空缓存数据库
     */
    @Override
    public void clear() {
        try {
            //redisClient.flushDb();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     * 获取缓存总数据
     */
    @Override
    public int getSize() {
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        return redisConnection.dbSize().intValue();
    }

    /**
     * 读写锁
     */
    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }
}
