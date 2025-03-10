package com.rmendes.sb_redis_test;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class RedisConfig {

    @Bean("sourceRedisConnectionFactory")
    public RedisConnectionFactory sourceRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("172.16.22.21", 12000);        
        return new JedisConnectionFactory(config);
    }

    @Bean("replicaRedisConnectionFactory")
    public RedisConnectionFactory replicaRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("172.16.22.23", 12001);
        return new JedisConnectionFactory(config);
    }

    @Bean("sourceRedisTemplate")
    public StringRedisTemplate sourceRedisTemplate(
            @Qualifier("sourceRedisConnectionFactory") RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean("replicaRedisTemplate")
    public StringRedisTemplate replicaRedisTemplate(
            @Qualifier("replicaRedisConnectionFactory") RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}
