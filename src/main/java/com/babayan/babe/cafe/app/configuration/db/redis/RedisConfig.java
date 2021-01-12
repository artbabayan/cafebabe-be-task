package com.babayan.babe.cafe.app.configuration.db.redis;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author artbabayan
 */
@Log4j2
@Configuration
@EnableCaching
public class RedisConfig {
    private static final int REDIS_DATABASE_INDEX = 0;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String passwd;

    @Value("${spring.redis.jedis.pool.max-active}")
    private Integer maxActive;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private Integer maxIdle;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private Long maxWait;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private Integer minIdle;

    @Bean
    public JedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setDatabase(REDIS_DATABASE_INDEX);

        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder poolingClientConfig =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();

        poolingClientConfig.poolConfig(jedisPoolConfig);
        JedisClientConfiguration jedisClientConfiguration = poolingClientConfig.build();

        log.info("host: {}", config.getHostName());
        log.info("port: {}", config.getPort());
        log.info("pass: {}", config.getPassword());
        log.info("max-active: {}", jedisPoolConfig.getMaxTotal());
        log.info("max-wait: {}", jedisPoolConfig.getMaxWaitMillis());
        log.info("min-idle:: {}", jedisPoolConfig.getMinIdle());

        return new JedisConnectionFactory(config, jedisClientConfiguration);
    }

    @Bean(name = "redisTemplates")
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory(jedisPool()));
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));

        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.create(redisConnectionFactory);
    }

    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig jedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMinIdle(minIdle);

        return jedisPoolConfig;
    }

}
