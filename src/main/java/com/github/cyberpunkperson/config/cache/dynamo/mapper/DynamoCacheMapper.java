package com.github.cyberpunkperson.config.cache.dynamo.mapper;


import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.databind.JavaType;

public interface DynamoCacheMapper {

    <TargetType> TargetType deserialize(AttributeValue value, JavaType targetType);

    AttributeValue serialize(Object value);

}
