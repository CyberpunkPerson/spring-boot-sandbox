package com.github.cyberpunkperson.config.cache;

import com.github.cyberpunkperson.config.cache.dynamo.DynamoCacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class MultipleCacheResolver implements CacheResolver {

    private final CacheManager cacheManager;

    private final CacheManager caffeineCacheMannager;


    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        Method targetMethod = context.getMethod();

        if (targetMethod.isAnnotationPresent(DynamoCacheable.class)) {
            return Stream.of(targetMethod.getAnnotation(DynamoCacheable.class).value())
                    .map(cacheManager::getCache)
                    .collect(toList());

        } else {
            return Stream.of(targetMethod.getAnnotation(Cacheable.class).value())
                    .map(cacheManager::getCache)
                    .collect(toList());
        }
    }
}
