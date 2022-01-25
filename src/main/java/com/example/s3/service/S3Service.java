package com.example.s3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    @Autowired
    S3Client s3Client;

    public void putEmptyObject(String bucketName, String key) {

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(request, RequestBody.empty());
    }

    public void createBucket(String bucketName) {

        System.out.println("Creating " + bucketName);

        try {
            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            s3Client.createBucket(bucketRequest);

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails()
                    .errorMessage());
        }
    }

    // To delete a bucket, all the objects in the bucket must be deleted first
    public void deleteBucketAndAllObjects(String bucketName) {

        System.out.println("Deleting " + bucketName);

        try {

            ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucketName).build();
            ListObjectsV2Response listObjectsV2Response;

            do {
                listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);
                for (S3Object s3Object : listObjectsV2Response.contents()) {
                    s3Client.deleteObject(DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(s3Object.key())
                            .build());
                }

                listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucketName)
                        .continuationToken(listObjectsV2Response.nextContinuationToken())
                        .build();

            } while(listObjectsV2Response.isTruncated());
            // snippet-end:[s3.java2.s3_bucket_ops.delete_bucket]

            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucketName).build();
            s3Client.deleteBucket(deleteBucketRequest);

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public boolean checkObjectExists(String bucketName, String prefix) {
        return getNumberOfObjects(bucketName, prefix) >= 1;
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
            System.err.println(e.awsErrorDetails().errorMessage());
        }
        return Collections.emptyList();
    }
}

