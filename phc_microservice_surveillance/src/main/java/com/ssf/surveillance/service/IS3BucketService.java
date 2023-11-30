package com.ssf.surveillance.service;

import com.ssf.surveillance.dtos.S3PresignedUrl;

public interface IS3BucketService {
    public S3PresignedUrl signBucket(String keyName);

    public S3PresignedUrl getImageUrl(String keyName);
}
