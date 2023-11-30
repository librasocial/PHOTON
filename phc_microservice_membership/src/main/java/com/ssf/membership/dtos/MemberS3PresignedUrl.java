package com.ssf.membership.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@NoArgsConstructor
public class MemberS3PresignedUrl {
    private URL preSignedUrl;
}
