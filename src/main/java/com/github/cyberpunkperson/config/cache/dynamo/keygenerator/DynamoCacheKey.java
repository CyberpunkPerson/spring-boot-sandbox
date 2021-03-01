package com.github.cyberpunkperson.config.cache.dynamo.keygenerator;

public interface DynamoCacheKey {

    String getCacheKey();

    Class<?> getTargetType();

}
