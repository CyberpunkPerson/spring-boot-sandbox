package com.github.cyberpunkperson.config.cache.dynamo.keygenerator;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class SimpleDynamoCacheKey implements DynamoCacheKey {

    String cacheKey;

    Class<?> targetType;

}
