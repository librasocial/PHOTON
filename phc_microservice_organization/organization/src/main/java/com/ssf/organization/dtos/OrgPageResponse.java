package com.ssf.organization.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrgPageResponse {
    private Long totalPages;
    private Long totalElements;
    private Object content;
}
