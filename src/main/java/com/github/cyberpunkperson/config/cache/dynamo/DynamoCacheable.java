package com.github.cyberpunkperson.config.cache.dynamo;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Cacheable
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamoCacheable {

    @AliasFor(annotation = Cacheable.class, attribute = "value")
    String[] value();

    @AliasFor(annotation = Cacheable.class, attribute = "keyGenerator")
    String keyGenerator();

    Class<?> returnType();

}
