package com.github.cyberpunkperson.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfig {


    @Bean
    public AWSCredentials amazonAWSCredentials(@Value("${cloud.aws.credentials.accessKey}") String accessKey,
                                               @Value("${cloud.aws.credentials.secretKey}") String secretKey) {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    @Bean
    public AWSCredentialsProvider amazonAWSCredentialsProvider(AWSCredentials amazonAWSCredentials) {
        return new AWSStaticCredentialsProvider(amazonAWSCredentials);
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB(@Value("${cloud.aws.region.static}") String region,
                                         AWSCredentialsProvider amazonAWSCredentialsProvider) {
        return AmazonDynamoDBClientBuilder.standard()
                .withCredentials(amazonAWSCredentialsProvider)
                .withRegion(region)
                .build();
    }

}
