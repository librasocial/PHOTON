package com.ssf.bssurveillance.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PageResponse {
    private PageDto meta;
    private Object data;
}
