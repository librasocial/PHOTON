package com.ssf.labtest.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class LabTestPageResponse {
    private PageDTO meta;
    private Object data;
}
