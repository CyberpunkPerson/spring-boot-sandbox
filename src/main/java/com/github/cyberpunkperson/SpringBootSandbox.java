package com.github.cyberpunkperson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpringBootSandbox {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSandbox.class, args);
    }

}
