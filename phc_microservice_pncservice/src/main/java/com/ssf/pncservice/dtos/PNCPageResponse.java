package com.ssf.pncservice.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PNCPageResponse {
    private PageDto meta;
    private Object data;
}
