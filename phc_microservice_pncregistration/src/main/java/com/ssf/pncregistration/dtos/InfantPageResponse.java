package com.ssf.pncregistration.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InfantPageResponse {
    private PageDto meta;
    private Object data;
}
