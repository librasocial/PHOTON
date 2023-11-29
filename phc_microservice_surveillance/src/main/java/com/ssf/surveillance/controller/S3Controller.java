package com.ssf.surveillance.controller;

import com.ssf.surveillance.dtos.S3PresignedUrl;
import com.ssf.surveillance.service.IS3BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class S3Controller {

    @Autowired
    private IS3BucketService s3BucketService;


    @GetMapping("/surveillance/getprefetchedurl")
    public ResponseEntity<S3PresignedUrl> getPreFetchedURL(
            @RequestParam(name = "bucketKey") String bucketKey) {
        return ResponseEntity.ok(s3BucketService.signBucket(bucketKey));
    }

    @GetMapping("/surveillance/getimageurl")
    public ResponseEntity<S3PresignedUrl> getImageURL(
            @RequestParam(name = "bucketKey") String bucketKey) {
        return ResponseEntity.ok(s3BucketService.getImageUrl(bucketKey));
    }

}
