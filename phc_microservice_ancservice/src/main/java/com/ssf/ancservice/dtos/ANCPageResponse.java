package com.ssf.ancservice.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ANCPageResponse {
    private PageDto meta;
    private Object data;
}
