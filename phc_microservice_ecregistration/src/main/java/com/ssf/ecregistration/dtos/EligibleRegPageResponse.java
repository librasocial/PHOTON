package com.ssf.ecregistration.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class EligibleRegPageResponse {
    private PageDto meta;
    private Object data;
}
