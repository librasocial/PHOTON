package com.ssf.surveillance.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@NoArgsConstructor
public class S3PresignedUrl {
    private URL preSignedUrl;
}
