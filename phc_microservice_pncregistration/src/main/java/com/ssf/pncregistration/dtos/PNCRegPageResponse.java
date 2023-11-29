package com.ssf.pncregistration.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PNCRegPageResponse {
    private PageDto meta;
    private Object data;
}
