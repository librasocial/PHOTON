package com.ssf.eligiblecouple.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class EligibleCouplePageResponse {
    private PageDto meta;
    private Object data;
}
