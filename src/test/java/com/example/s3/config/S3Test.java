package com.example.s3.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = {AwsS3Configuration.class})
@TestPropertySource(properties = {
        "cloud.aws.credentials.access-key=",
        "cloud.aws.credentials.secret=",
        "cloud.aws.region=eu-west-1"
})
public class S3Test {

    @Autowired
    S3Client s3Client;

    @Test
    public void testPutBucket() {
        putObject("dobie-test-bucket", "test9");
    }

    @Test
    public void testNoBucketFails() {
        assertEquals(checkBucketExists("dobie-test-bucketdsasd"), false);
    }

    @Test
    public void testRealBucketExists() {

        assertEquals(checkBucketExists("dobie-test-bucket"), true);
    }

    @Test
    public void testBucketDoesNotExist() {

        assertEquals(checkBucketExists("dobie-test-bucket"), true);
    }

/*    @Test
    public void testRealSubBucketExists() {
        String TEST_SUB_BUCKET = "dobie-test-bucket/test9";
        assertThat(checkBucketExists(TEST_SUB_BUCKET)).isTrue();
    }*/

    @Test
    public void testListBucketObjects() {
        assertThat(listBucketObjects("dobie-test-bucket", "").containsAll(Arrays.asList("test, test1")));
    }


    @Test
    public void testDirectoryExists() {
        assertThat(listBucketObjects("dobie-test-bucket", "test/test2/")).hasSizeGreaterThan(0);
    }

    @Test
    public void testObjectExists() {
        assertThat(listBucketObjects("dobie-test-bucket", "test/test2/aprc-simulator.xls")).hasSize(1);
    }

    @Test
    public void testObjectDoesNotExist() {
        assertThat(listBucketObjects("dobie-test-bucket", "test/test2/nonsense.xls")).hasSize(0);
    }

    public void testHeadBucket() {
        //headBuckets();
    }

    public void createBucket(String bucketName) {

        try {
            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            s3Client.createBucket(bucketRequest);

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails()
                    .errorMessage());
            System.exit(1);
        }
    }

    public void putObject(String bucketName, String key) {

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(request, RequestBody.empty());
    }

    private boolean doesBucketExist(String bucketName) {
        HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                .bucket(bucketName).
                build();
        try {
            HeadBucketResponse headBucketsResponse = s3Client.headBucket(headBucketRequest);
            return true;
        } catch (NoSuchBucketException noSuchBucketException) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean objectExists(String bucketName, String prefix) {
        return getNumberOfObjects(bucketName, prefix) == 1;
    }

    private int getNumberOfObjects(String bucketName, String prefix) {
        int items = listBucketObjects(bucketName, prefix).size();
        return items;
    }

    public List<String> listBucketObjects(String bucketName, String prefix) {
        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .build();

            ListObjectsResponse res = s3Client.listObjects(listObjects);

            List<String> keyList = res.contents()
                    .stream()
                    .map(results -> results.key())
                    .collect(Collectors.toList());

            return keyList;

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails()
                    .errorMessage());
        }
        return Collections.emptyList();
    }

    public boolean checkBucketExists(String bucketName) {
        HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                .bucket(bucketName)
                .build();

        try {
            s3Client.headBucket(headBucketRequest);
            return true;

        } catch (S3Exception ex) {
            if (ex.statusCode() == 403 || ex.statusCode() == 400) {
                System.out.println(ex.getMessage());
            } else if (ex.statusCode() == 404) {
                System.out.println("This bucket doesn't exist");
            }
        }
        return false;
    }

    private static long calKb(Long val) {
        return val / 1024;
    }

}
