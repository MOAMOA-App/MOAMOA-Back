package org.zerock.moamoa.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class ViewsRedisConfig extends RedisConfig {
    @Bean
    public RedisConnectionFactory viewsRedisConnectionFactory() {
        return createLettuceConnectionFactory(2); // Redis DB 선택
    }

    @Bean(name = "viewsRedisTemplate")
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(viewsRedisConnectionFactory());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
