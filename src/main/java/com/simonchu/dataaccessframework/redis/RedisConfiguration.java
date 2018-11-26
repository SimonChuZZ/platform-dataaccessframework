package com.simonchu.dataaccessframework.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.simonchu.dataaccessframework.database.MybatisRedisCache;

import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis 配置类
 *
 * @author zhaozan.chu
 * @date 2017年6月20日 上午10:20:47
 */
@Configuration("redisConfiguration")
@EnableAutoConfiguration
public class RedisConfiguration {
	@Autowired
	private RedisManager redisManager;

	/**
	 * 构建动态Redis 数据源
	 * @return
	 */
	@Bean("dynamicRedis")
	public DynamicRedis getDynamicRedis(){
		JedisPoolConfig jedisPoolConfig = redisManager.buildRedisConfig();
		JedisConnectionFactory jedisConnectionFactory = redisManager.buildJedisConnectionFactory(jedisPoolConfig);
		RedisTemplate<String, Object> objectRedisTemplate = redisManager.buildObjectRedisTemplate(jedisConnectionFactory);
		RedisTemplate<String, String> stringRedisTemplate = redisManager.buildStringRedisTemplate(jedisConnectionFactory);
		return new DynamicRedis(objectRedisTemplate,stringRedisTemplate);
	}

	/**
	 * 生成Redis Object模板对象
	 * @param dynamicRedis 动态数据源
	 * @return
	 */
	@Bean("objectRedisTemplate")
	@Autowired
	public RedisTemplate<String, Object> getObjectRedisTemplate(@Qualifier("dynamicRedis") DynamicRedis dynamicRedis) {
		return dynamicRedis.getObjectRedisTemplate();
	}

	/**
	 * 生成Redis String 操作模板对象
	 * @param dynamicRedis 动态数据源
	 * @return
	 */
	@Bean(name = "stringRedisTemplate")
	@Autowired
	public RedisTemplate<String, String> getStringRedisTemplate(@Qualifier("dynamicRedis") DynamicRedis dynamicRedis) {
		return dynamicRedis.getStringRedisTemplate();
	}

	/**
	 * 设置缓存Redis 连接池
	 *
	 * @param redisTemplate
	 *            redis 模板
	 * @return
	 * @author zhaozan.chu
	 * @date 2017年6月22日 下午3:31:49
	 */
	@Bean(name = "setMyBatisRedisCache")
	@Autowired
	public int setMyBatisRedisCache(@Qualifier("objectRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
		MybatisRedisCache.setRedisClient(redisTemplate);
		return 0;
	}
}
