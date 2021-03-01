package com.github.cyberpunkperson.config.cache.dynamo.context;

import com.github.cyberpunkperson.config.cache.dynamo.keygenerator.DynamoCacheKey;
import com.github.cyberpunkperson.config.cache.dynamo.repository.DynamoCacheRepository;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface DynamoCacheContext {

    DynamoCacheRepository getNativeCache();

    <TargetType> Optional<TargetType> get(String cacheName, String key, Class<TargetType> targetType);

    void put(String cacheName, DynamoCacheKey key, @Nullable Object value);

    void evict(String cacheName, String key);

    void clear(String cacheName);

}
