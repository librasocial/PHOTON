package com.ssf.surveillance.service;


import com.ssf.surveillance.dtos.S3PresignedUrl;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;


@Service
public  class S3BucketConfig implements IS3BucketService {
    String bucketName = "ssf-dev-assets";
    Region region = Region.AP_SOUTH_1;

    AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create("AKIAR2KY2Z47FONLTPZX", "Q0dwDIIf5On7N581scWo28m84N2vFaXRJ6KWXXbb"));
    S3Presigner presigner = S3Presigner.builder()
            .region(region)
            .credentialsProvider(credentialsProvider)
            .build();

    public S3PresignedUrl signBucket(String keyName) {
        URL url = null;
        S3PresignedUrl s3PresignedUrl = new S3PresignedUrl();
        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key("surveillance-assets/" + keyName)
                    .contentType("image/jpeg")
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);


            String myURL = presignedRequest.url().toString();

            // Upload content to the Amazon S3 bucket by using this URL
            url = presignedRequest.url();
            s3PresignedUrl.setPreSignedUrl(url);
            presigner.close();

        } catch (S3Exception e) {
            e.getStackTrace();
        }
        return s3PresignedUrl;
    }

    public S3PresignedUrl getImageUrl(String keyName ) {
        URL url = null;
        S3PresignedUrl s3PresignedUrl = new S3PresignedUrl();
        try {
            GetObjectRequest getObjectRequest =
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key("surveillance-assets/" + keyName)
                            .build();

            GetObjectPresignRequest getObjectPresignRequest =  GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(getObjectRequest)
                    .build();

            // Generate the presigned request
            PresignedGetObjectRequest presignedGetObjectRequest =
                    presigner.presignGetObject(getObjectPresignRequest);

            // Load the presigned URL
            url = presignedGetObjectRequest.url();
            s3PresignedUrl.setPreSignedUrl(url);
            presigner.close();

        } catch (S3Exception e) {
            e.getStackTrace();
        }
        return s3PresignedUrl;
    }
}

