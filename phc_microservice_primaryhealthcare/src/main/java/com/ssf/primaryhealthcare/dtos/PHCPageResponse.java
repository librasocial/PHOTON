package com.ssf.primaryhealthcare.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PHCPageResponse {
    private Long totalPages;
    private Long totalElements;
    private Object content;
}
