package com.github.cyberpunkperson.config.cache.dynamo.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.github.cyberpunkperson.config.cache.dynamo.DynamoAttribute.ATTRIBUTE_KEY;
import static com.github.cyberpunkperson.config.cache.dynamo.DynamoAttribute.ATTRIBUTE_VALUE;
import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.Assert.hasText;

@Repository
@RequiredArgsConstructor
public class DefaultDynamoCacheRepository implements DynamoCacheRepository {

    private final AmazonDynamoDB dynamoTemplate;


    @Override
    public AmazonDynamoDB getNativeCacheRepository() {
        return this.dynamoTemplate;
    }

    @Override
    public void put(String cacheName, Map<String, AttributeValue> attributes) {
        hasText(cacheName, "Cache name must not be null or empty string");

        dynamoTemplate.putItem(
                new PutItemRequest()
                        .withTableName(cacheName)
                        .withItem(attributes));
    }

    @Override
    public GetItemResult get(String cacheName, String key) {
        hasText(cacheName, "Cache name must not be null or empty string");
        hasText(key, "Cache key must not be null or empty string");

        return dynamoTemplate.getItem(
                new GetItemRequest()
                        .withAttributesToGet(ATTRIBUTE_VALUE)
                        .withTableName(cacheName)
                        .withKey(singletonMap(ATTRIBUTE_KEY, new AttributeValue(key))));
    }

    @Override
    public void delete(String cacheName, String key) {
        hasText(cacheName, "Cache name must not be null or empty string");
        hasText(key, "Cache key must not be null or empty string");

        dynamoTemplate.deleteItem(
                new DeleteItemRequest()
                        .withTableName(cacheName)
                        .withKey(singletonMap(ATTRIBUTE_KEY, new AttributeValue(key))));
    }

    @Override
    public void clear(String cacheName) {
        hasText(cacheName, "Cache name must not be null or empty string");

        List<WriteRequest> deleteRequests = dynamoTemplate.scan(new ScanRequest(cacheName)).getItems().stream()
                .map(item -> new WriteRequest(new DeleteRequest(Map.of(ATTRIBUTE_KEY, item.get(ATTRIBUTE_KEY)))))
                .collect(toList());

        dynamoTemplate.batchWriteItem(new BatchWriteItemRequest(Map.of(cacheName, deleteRequests)));
    }

}
