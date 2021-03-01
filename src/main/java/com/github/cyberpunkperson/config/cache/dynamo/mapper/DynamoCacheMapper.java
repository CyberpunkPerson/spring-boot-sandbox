package com.github.cyberpunkperson.config.cache.dynamo.mapper;


import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public interface DynamoCacheMapper {

    <TargetType> TargetType deserialize(AttributeValue value, Class<TargetType> targetType);

    AttributeValue serialize(Object value);

}
