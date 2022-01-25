package com.example.s3.service;

import com.example.s3.config.AwsS3Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {AwsS3Configuration.class, S3Service.class})
public class S3ServiceTest {

    private static final String TEST_BUCKET_NAME="dobie-test-bucket";

    private static final String TEST_FOLDER_1 = "test1/";
    private static final String TEST_FOLDER_2 = "test2/";
    private static final String TEST_FOLDER_3 = "test2/test3/";
    private static final String TEST_FILE = TEST_FOLDER_3 + "empty.txt";

    @Autowired
    S3Service s3Service;

    @BeforeAll
    public void setup(){
        s3Service.createBucket(TEST_BUCKET_NAME);
        s3Service.putEmptyObject(TEST_BUCKET_NAME, TEST_FOLDER_1);
        s3Service.putEmptyObject(TEST_BUCKET_NAME, TEST_FOLDER_2);
        s3Service.putEmptyObject(TEST_BUCKET_NAME, TEST_FOLDER_3);
        s3Service.putEmptyObject(TEST_BUCKET_NAME, TEST_FILE);
    }

    @AfterAll
    public void teardown(){
        s3Service.deleteBucketAndAllObjects(TEST_BUCKET_NAME);
    }

    @Test
    public void testNoBucketFails() {
        assertThat(s3Service.checkObjectExists(TEST_BUCKET_NAME, "NONSENSE")).isFalse();
    }

    @Test
    public void testBucketExists() {
        assertThat(s3Service.checkObjectExists(TEST_BUCKET_NAME, "")).isTrue();
    }

    @Test
    public void testFolderExists() {
        assertThat(s3Service.checkObjectExists(TEST_BUCKET_NAME, TEST_FOLDER_3)).isTrue();
    }

    @Test
    public void testFileExists() {
        assertThat(s3Service.checkObjectExists(TEST_BUCKET_NAME, TEST_FILE)).isTrue();
    }

    @Test
    public void testListBucketObjects() {
        assertThat(s3Service.listBucketObjects(TEST_BUCKET_NAME, "").containsAll(Arrays.asList(TEST_FOLDER_1, TEST_FOLDER_2, TEST_FOLDER_3)));
    }

    @Test
    public void testListFolderObjects() {
        assertThat(s3Service.listBucketObjects(TEST_BUCKET_NAME, TEST_FOLDER_2).containsAll(Arrays.asList(TEST_FOLDER_2, TEST_FOLDER_3)));
    }

}
