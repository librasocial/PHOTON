package com.ssf.membership.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberPageResponse {
    private Long totalPages;
    private Long totalElements;
    private Object content;
}
