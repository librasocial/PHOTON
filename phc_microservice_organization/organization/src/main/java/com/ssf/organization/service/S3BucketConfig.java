package com.ssf.organization.service;


import com.ssf.organization.config.ApplicationSecretConfig;
import com.ssf.organization.dtos.OrgS3PresignedUrl;
import com.ssf.organization.utils.SecretsConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
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
public class S3BucketConfig implements IS3BucketService {
    private S3Presigner presigner;

    @Autowired
    private Environment env;

    @Value("${aws.region}")
    private String region;

    @Autowired
    private ApplicationSecretConfig applicationSecretConfig;

    @Bean
    private void setS3Presigner() {
        AwsCredentialsProvider credentialsProvider =
                StaticCredentialsProvider.create(AwsBasicCredentials.create(env.getProperty("aws.access.key"), env.getProperty("aws.secret.key")));

        this.presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    public OrgS3PresignedUrl signBucket(String assetType, String keyName) {
        URL url = null;
        OrgS3PresignedUrl memberS3PresignedUrl = new OrgS3PresignedUrl();
        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(applicationSecretConfig.getSecretKey(SecretsConstant.S3_BUCKET_NAME))
                    .key(assetType + "/" + keyName)
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
            memberS3PresignedUrl.setPreSignedUrl(url);
            presigner.close();

        } catch (S3Exception e) {
            e.getStackTrace();
        }
        return memberS3PresignedUrl;
    }

    public OrgS3PresignedUrl getImageUrl(String assetType, String keyName) {
        URL url = null;
        OrgS3PresignedUrl memberS3PresignedUrl = new OrgS3PresignedUrl();
        try {
            GetObjectRequest getObjectRequest =
                    GetObjectRequest.builder()
                            .bucket(applicationSecretConfig.getSecretKey(SecretsConstant.S3_BUCKET_NAME))
                            .key(assetType + "/" + keyName)
                            .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(getObjectRequest)
                    .build();

            // Generate the presigned request
            PresignedGetObjectRequest presignedGetObjectRequest =
                    presigner.presignGetObject(getObjectPresignRequest);

            // Load the presigned URL
            url = presignedGetObjectRequest.url();
            memberS3PresignedUrl.setPreSignedUrl(url);
            presigner.close();

        } catch (S3Exception e) {
            e.getStackTrace();
        }
        return memberS3PresignedUrl;
    }
}

