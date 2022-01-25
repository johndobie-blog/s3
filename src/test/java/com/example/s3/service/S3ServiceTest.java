package com.example.s3.service;

import com.example.s3.config.AwsS3Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {AwsS3Configuration.class, S3Service.class})
@TestPropertySource(properties = {
        "cloud.aws.credentials.access-key=",
        "cloud.aws.credentials.secret=",
        "cloud.aws.region=eu-west-1"
})
public class S3ServiceTest {

    private static final String TEST_BUCKET_NAME="dobie-test-bucket";

    private static final String TEST_FOLDER_1 = "test1/";
    private static final String TEST_FOLDER_2 = "test2/";
    private static final String TEST_FOLDER_3 = "test2/test3/";

    @Autowired
    S3Service s3Service;

    @BeforeAll
    public void setup(){
        s3Service.createBucket(TEST_BUCKET_NAME);
        s3Service.putObject(TEST_BUCKET_NAME, TEST_FOLDER_1);
        s3Service.putObject(TEST_BUCKET_NAME, TEST_FOLDER_2);
        s3Service.putObject(TEST_BUCKET_NAME, TEST_FOLDER_3);
    }

    @AfterAll
    public void teardown(){
        s3Service.deleteBucketAndAllObjects(TEST_BUCKET_NAME);
    }

    @Test
    public void testPutBucket() {

    }

/*    @Test
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

*//*    @Test
    public void testRealSubBucketExists() {
        String TEST_SUB_BUCKET = "dobie-test-bucket/test9";
        assertThat(checkBucketExists(TEST_SUB_BUCKET)).isTrue();
    }*//*

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
            System.err.println(e.awsErrorDetails().errorMessage());
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
    }*/


}
