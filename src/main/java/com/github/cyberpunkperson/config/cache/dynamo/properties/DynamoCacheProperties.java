package com.github.cyberpunkperson.config.cache.dynamo.properties;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
public class DynamoCacheProperties {

    private final String name;

    private final Duration ttl;

    private final boolean flushOnBoot;

}
