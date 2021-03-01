package com.github.cyberpunkperson.config.cache.dynamo.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.cache.dynamo")
public class DynamoCacheDescriptor {

    private final List<DynamoCacheProperties> caches;

}
