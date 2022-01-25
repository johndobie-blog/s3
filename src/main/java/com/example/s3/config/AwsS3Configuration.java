package com.example.s3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Configuration {

    @Value("${cloud.aws.credentials.access-key:null}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret:null}")
    private String secretKey;

    @Value("${cloud.aws.region:ap-southeast-1}")
    private String region;

    @Bean
    public Region getRegion() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return Region.of(region);
    }

    @Bean
    public AwsBasicCredentials getAWSCredentials() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return credentials;
    }

    @Bean
    public S3Client getAmazonSS(AwsBasicCredentials awsCredentials, Region region) {
        S3Client s3client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(region)
                .build();
        return s3client;
    }
}
