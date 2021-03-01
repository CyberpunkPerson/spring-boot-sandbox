package com.github.cyberpunkperson.config.cache.dynamo.context;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.github.cyberpunkperson.config.cache.dynamo.keygenerator.DynamoCacheKey;
import com.github.cyberpunkperson.config.cache.dynamo.mapper.DynamoCacheMapper;
import com.github.cyberpunkperson.config.cache.dynamo.repository.DynamoCacheRepository;
import lombok.Builder;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.github.cyberpunkperson.config.cache.dynamo.DynamoAttribute.*;
import static java.util.Objects.nonNull;
import static org.springframework.util.Assert.hasText;

@Builder(builderMethodName = "hiddenBuilder")
public class DefaultDynamoCacheContext implements DynamoCacheContext {

    private final Duration ttl;

    private final DynamoCacheRepository cacheRepository;

    private final DynamoCacheMapper cacheMapper;


    public static DefaultDynamoCacheContextBuilder builder(DynamoCacheRepository cacheRepository, DynamoCacheMapper cacheMapper) {
        return hiddenBuilder().cacheRepository(cacheRepository).cacheMapper(cacheMapper);
    }

    @Override
    public DynamoCacheRepository getNativeCache() {
        return cacheRepository;
    }

    @Override
    public <TargetType> Optional<TargetType> get(String cacheName, String key, Class<TargetType> targetType) {
        hasText(cacheName, "Cache name must not be null or empty string");
        hasText(key, "Cache key must not be null or empty string");

        return Optional.ofNullable(cacheRepository.get(cacheName, key).getItem())
                .map(result -> cacheMapper.deserialize(result.get(ATTRIBUTE_VALUE), targetType));
    }

    @Override
    public void put(String cacheName, DynamoCacheKey key, @Nullable Object value) {
        hasText(cacheName, "Cache name must not be null or empty string");
        cacheRepository.put(cacheName, getPutAttributes(key.getCacheKey(), value));
    }

    private Map<String, AttributeValue> getPutAttributes(String key, Object value) {

        Map<String, AttributeValue> attributes = new HashMap<>();
        attributes.put(ATTRIBUTE_KEY, new AttributeValue().withS(key));

        if (nonNull(value)) {
            attributes.put(ATTRIBUTE_VALUE, cacheMapper.serialize(value));
        } else {
            attributes.put(ATTRIBUTE_VALUE, new AttributeValue().withNULL(true));
        }

        if (isTTLValid(ttl)) {
            attributes.put(ATTRIBUTE_TTL, new AttributeValue().withN(String.valueOf(Instant.now().plus(ttl).getEpochSecond())));
        }

        return attributes;
    }

    private static boolean isTTLValid(Duration ttl) {
        return ttl != null && !ttl.isZero() && !ttl.isNegative();
    }

    @Override
    public void evict(String cacheName, String key) {
        cacheRepository.delete(cacheName, key);
    }

    @Override
    public void clear(String cacheName) {
        cacheRepository.clear(cacheName);
    }

}
