package com.ssf.organization.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@NoArgsConstructor
public class OrgS3PresignedUrl {
    private URL preSignedUrl;
}
