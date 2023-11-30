package com.ssf.labtest.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageDTO {
    private Long totalPages;
    private Long totalElements;
    private Integer number;
    private Integer size;
}
