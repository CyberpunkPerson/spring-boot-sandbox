package com.github.cyberpunkperson.config.cache.dynamo.mapper;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonDynamoCacheMapper implements DynamoCacheMapper {

    private final ObjectMapper objectMapper;


    @Override
    @SneakyThrows
    public <TargetType> TargetType deserialize(AttributeValue value, Class<TargetType> targetType) {
        return objectMapper.readValue(value.getS(), targetType);
    }

    @Override
    @SneakyThrows
    public AttributeValue serialize(Object value) {
        return new AttributeValue().withS(objectMapper.writeValueAsString(value));
    }

}
