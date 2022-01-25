package com.example.s3.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import software.amazon.awssdk.regions.Region;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = {AwsS3Configuration.class})
@TestPropertySource(properties = {
        "cloud.aws.credentials.access-key=access-key",
        "cloud.aws.credentials.secret=secret",
        "cloud.aws.region=ap-southeast-1"
})
public class AwsS3ConfigurationTest {

    @Autowired
    AwsS3Configuration s3Configuration;

    @Autowired
    Region region;

    @Test
    public void testAccessKeyConfig() {
        assertEquals(s3Configuration.getAWSCredentials().accessKeyId(), "access-key");
    }

    @Test
    public void testSecretConfig() {
        assertEquals(s3Configuration.getAWSCredentials().secretAccessKey(), "secret");
    }

    @Test
    public void testRegionConfig() {
        assertEquals(region, Region.AP_SOUTHEAST_1);
    }
}
