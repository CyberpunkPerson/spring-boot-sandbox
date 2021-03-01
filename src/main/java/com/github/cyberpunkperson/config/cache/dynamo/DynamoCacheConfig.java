package com.github.cyberpunkperson.config.cache.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.github.cyberpunkperson.config.cache.dynamo.context.DefaultDynamoCacheContext;
import com.github.cyberpunkperson.config.cache.dynamo.mapper.DynamoCacheMapper;
import com.github.cyberpunkperson.config.cache.dynamo.properties.DynamoCacheDescriptor;
import com.github.cyberpunkperson.config.cache.dynamo.repository.DynamoCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Configuration
@EnableCaching
@RequiredArgsConstructor
@ConditionalOnClass(AmazonDynamoDB.class)
public class DynamoCacheConfig {

    private final DynamoCacheDescriptor dynamoCacheDescriptor;

    private final DynamoCacheMapper jsonDynamoCacheMapper;

    private final DynamoCacheRepository cacheRepository;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(buildCaches());
        return cacheManager;
    }

    private List<DynamoCache> buildCaches() {

        return dynamoCacheDescriptor.getCaches().stream()
                .map(cacheProperties ->
                        new DynamoCache(cacheProperties.getName(),
                                DefaultDynamoCacheContext.builder(cacheRepository, jsonDynamoCacheMapper)
                                        .ttl(cacheProperties.getTtl())
                                        .build()
                        ))
                .collect(toList());
    }

}
