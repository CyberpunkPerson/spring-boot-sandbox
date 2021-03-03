package com.github.cyberpunkperson.config.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CustomCachingConfigurerSupport extends CachingConfigurerSupport {

    private final CacheResolver cacheResolver;

    @Override
    public CacheResolver cacheResolver() {
        return cacheResolver;
    }
}
