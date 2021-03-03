package com.github.cyberpunkperson.config.cache.dynamo.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;

import java.util.Map;

public interface DynamoCacheRepository {

    AmazonDynamoDB getNativeCacheRepository();

    void put(String cacheName, Map<String, AttributeValue> attributes);

    GetItemResult get(String cacheName, String key);

    void delete(String cacheName, String key);

    void clear(String cacheName);

}
