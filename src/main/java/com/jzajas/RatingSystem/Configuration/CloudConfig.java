package com.jzajas.RatingSystem.Configuration;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.sql.DataSource;

@Configuration
@Profile("cloud")
public class CloudConfig extends AbstractCloudConfig {
    @Bean
    @Primary
    public DataSource dataSource() {
        return connectionFactory().dataSource("postgres");
    }

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        return connectionFactory().redisConnectionFactory("redis");
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }
}
