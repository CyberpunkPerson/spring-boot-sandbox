package com.github.cyberpunkperson.config.cache.dynamo.keygenerator;

import com.fasterxml.jackson.databind.JavaType;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class SimpleDynamoCacheKey implements DynamoCacheKey {

    String cacheKey;

    JavaType targetType;

}
