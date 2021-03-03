package com.github.cyberpunkperson.config.cache.dynamo.keygenerator;

import com.fasterxml.jackson.databind.JavaType;

public interface DynamoCacheKey {

    String getCacheKey();

    JavaType getTargetType();

}
