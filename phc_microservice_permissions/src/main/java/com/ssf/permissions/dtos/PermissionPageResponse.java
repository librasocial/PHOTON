package com.ssf.permissions.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionPageResponse {
    private Long totalPages;
    private Long totalElements;
    private Object content;
}
