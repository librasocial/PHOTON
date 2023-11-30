package com.ssf.organization.service;


import com.ssf.organization.dtos.OrgS3PresignedUrl;

public interface IS3BucketService {
    public OrgS3PresignedUrl signBucket(String assetType, String keyName);

    public OrgS3PresignedUrl getImageUrl(String assetType, String keyName);
}
