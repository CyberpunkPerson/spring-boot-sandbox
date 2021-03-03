package com.github.cyberpunkperson.config.cache.dynamo.mapper;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
@RequiredArgsConstructor
public class ByteDynamoCacheMapper implements DynamoCacheMapper {

    private final ObjectMapper objectMapper;


    @Override
    @SneakyThrows
    public <TargetType> TargetType deserialize(AttributeValue value, JavaType targetType) {
        return objectMapper.readValue(value.getB().array(), targetType);
    }

    @Override
    @SneakyThrows
    public AttributeValue serialize(Object value) {
        return new AttributeValue().withB(ByteBuffer.wrap(objectMapper.writeValueAsBytes(value)));
    }

}
