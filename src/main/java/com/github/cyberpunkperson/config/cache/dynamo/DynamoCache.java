package com.github.cyberpunkperson.config.cache.dynamo;

import com.github.cyberpunkperson.config.cache.dynamo.context.DynamoCacheContext;
import com.github.cyberpunkperson.config.cache.dynamo.keygenerator.DynamoCacheKey;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.Optional;
import java.util.concurrent.Callable;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

@Data
@RequiredArgsConstructor
public class DynamoCache implements Cache {

    private final String cacheName;

    private final DynamoCacheContext context;


    @Override
    public String getName() {
        return this.cacheName;
    }

    @Override
    public Object getNativeCache() {
        return this.context.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        isTrue(key instanceof DynamoCacheKey, "'key' must be an instance of '%s'".formatted(DynamoCacheKey.class.getName()));

        return this.getFromCache((DynamoCacheKey) key)
                .map(SimpleValueWrapper::new)
                .orElse(null);
    }

    @Override
    public <T> T get(Object key, Class<T> targetType) {
        isTrue(key instanceof DynamoCacheKey, "'key' must be an instance of '%s'".formatted(DynamoCacheKey.class.getName()));
        notNull(targetType, "Target type must not be null");

        return this.getFromCache((DynamoCacheKey) key)
                .map(targetType::cast)
                .orElse(null);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        isTrue(key instanceof DynamoCacheKey, "'key' must be an instance of '%s'".formatted(DynamoCacheKey.class.getName()));
        notNull(valueLoader, "'valueLoader' must not be null.");

        return this.getFromCache((DynamoCacheKey) key)
                .map(value -> (T) value)
                .orElseGet(() -> {
                    try {
                        T value = valueLoader.call();
                        put(key, value);
                        return value;
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                });
    }

    @Override
    public void put(Object key, Object value) {
        isTrue(key instanceof DynamoCacheKey, "'key' must be an instance of '%s'".formatted(DynamoCacheKey.class.getName()));
        this.context.put(cacheName, (DynamoCacheKey) key, value);
    }

    @Override
    public void evict(Object key) {
        isTrue(key instanceof DynamoCacheKey, "'key' must be an instance of '%s'".formatted(DynamoCacheKey.class.getName()));
        this.context.evict(cacheName, ((DynamoCacheKey) key).getCacheKey());
    }

    @Override
    public void clear() {
        this.context.clear(cacheName);
    }

    private Optional<?> getFromCache(DynamoCacheKey key) {
        return this.context.get(cacheName, key.getCacheKey(), key.getTargetType());
    }

}
