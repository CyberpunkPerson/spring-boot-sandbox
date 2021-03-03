package com.github.cyberpunkperson.config.cache.dynamo.keygenerator;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cyberpunkperson.config.cache.CacheKey;
import com.github.cyberpunkperson.config.cache.dynamo.DynamoCacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.stream.Stream;

import static org.springframework.util.Assert.isTrue;

@Component
@RequiredArgsConstructor
public class DynamoCacheKeyGenerator implements KeyGenerator {

    private final ObjectMapper objectMapper;


    @Override
    @SuppressWarnings("unchecked")
    public DynamoCacheKey generate(Object target, Method method, Object... params) {
        DynamoCacheable cacheable = method.getAnnotation(DynamoCacheable.class);
        Class<?> genericReturnType = cacheable.returnType();

        isTrue(!Modifier.isAbstract(genericReturnType.getModifiers())
                        && !Modifier.isInterface(genericReturnType.getModifiers()),
                String.format("Attribute 'returnType' of '@%s' can not be an abstract type or interface",
                        DynamoCacheable.class.getName()));

        CacheKey cacheKey = Stream.of(params)
                .filter(param -> CacheKey.class.isAssignableFrom(param.getClass()))
                .findFirst()
                .map(CacheKey.class::cast)
                .orElseThrow(() -> new IllegalStateException("There is no parameter implements '%s'"
                        .formatted(CacheKey.class.getName())));

        Class<?> methodReturnType = method.getReturnType();
        JavaType returnType;
        if (Collection.class.isAssignableFrom(methodReturnType)) {
            returnType = objectMapper.getTypeFactory()
                    .constructCollectionType((Class<? extends Collection<?>>) methodReturnType, genericReturnType);
        } else {
            returnType = objectMapper.constructType(genericReturnType);
        }

        return new SimpleDynamoCacheKey(cacheKey.buildCacheKey(), returnType);
    }
}
