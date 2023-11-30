package com.ssf.inward.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PageResponse {
    private PageDto meta;
    private Object data;
}
