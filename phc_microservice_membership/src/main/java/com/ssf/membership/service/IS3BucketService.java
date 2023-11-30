package com.ssf.membership.service;

import com.ssf.membership.dtos.MemberS3PresignedUrl;

public interface IS3BucketService {
    public MemberS3PresignedUrl signBucket(String assetType, String keyName);

    public MemberS3PresignedUrl getImageUrl(String assetType, String keyName);
}
