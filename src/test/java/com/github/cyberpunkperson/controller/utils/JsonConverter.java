package com.github.cyberpunkperson.controller.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class JsonConverter {

    private final ObjectMapper objectMapper;


    @SneakyThrows
    public String writeToJson(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    @SneakyThrows
    public <TargetType> TargetType readJson(MvcResult result, Class<TargetType> targetType) {
        return objectMapper.readValue(result.getResponse().getContentAsString(), targetType);
    }

    public JavaType constructType(Class<?> type) {
        return objectMapper.constructType(type);
    }

    public JavaType constrctCollectionType(Class<? extends Collection> collectionType, Class<?> type) {
        return objectMapper.getTypeFactory().constructCollectionType(collectionType, type);
    }

}
