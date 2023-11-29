package com.ssf.ancregistration.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ANCRegPageResponse {
    private PageDto meta;
    private Object data;
}
