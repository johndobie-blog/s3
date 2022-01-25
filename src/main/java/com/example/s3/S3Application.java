package com.example.s3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;

@SpringBootApplication
public class S3Application {

    public static void main(String[] args) {
        SpringApplication.run(S3Application.class, args);
    }

}

