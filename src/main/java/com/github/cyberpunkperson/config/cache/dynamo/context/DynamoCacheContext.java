package com.github.cyberpunkperson.config.cache.dynamo.context;

import com.fasterxml.jackson.databind.JavaType;
import com.github.cyberpunkperson.config.cache.dynamo.repository.DynamoCacheRepository;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface DynamoCacheContext {

    DynamoCacheRepository getNativeCache();

     Optional<Object> get(String cacheName, String key, JavaType targetType);

    void put(String cacheName, String key, @Nullable Object value);

    void evict(String cacheName, String key);

    void clear(String cacheName);

}
