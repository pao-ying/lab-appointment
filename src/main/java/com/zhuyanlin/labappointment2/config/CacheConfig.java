package com.zhuyanlin.labappointment2.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        // 创建Caffeine缓存管理器
        CaffeineCacheManager manager = new CaffeineCacheManager();
        // 创建缓存配置策略
        Cache<Object, Object> cache = Caffeine.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .maximumSize(200)
                .build();
        // 为指定名称的缓存，指定配置
        manager.registerCustomCache("allAppointment", cache);
        manager.registerCustomCache("teacherAppointment", cache);
        // 允许缓存值为空的键值对。避免缓存穿透
        manager.setAllowNullValues(true);
        // 将管理器注入容器，替换默认管理器对象
        return manager;
    }
}
