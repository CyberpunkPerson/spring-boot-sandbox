package com.github.cyberpunkperson.config.cache.dynamo.keygenerator;

import com.github.cyberpunkperson.config.cache.CacheKey;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

import static org.springframework.util.Assert.isTrue;

@Component
public class DynamoCacheKeyGenerator implements KeyGenerator {


    @Override
    public DynamoCacheKey generate(Object target, Method method, Object... params) {
        Class<?> returnType = method.getReturnType();

        isTrue(!Modifier.isAbstract(returnType.getModifiers())
                        && !Modifier.isInterface(returnType.getModifiers()),
                "Return type of cache can not be an abstract type or interface");

        CacheKey cacheKey = Stream.of(params)
                .filter(param -> CacheKey.class.isAssignableFrom(param.getClass()))
                .findFirst()
                .map(CacheKey.class::cast)
                .orElseThrow(() -> new IllegalStateException("There is no parameter implements '%s'"
                        .formatted(CacheKey.class.getName())));

        return new SimpleDynamoCacheKey(cacheKey.buildCacheKey(), returnType);
    }
}
