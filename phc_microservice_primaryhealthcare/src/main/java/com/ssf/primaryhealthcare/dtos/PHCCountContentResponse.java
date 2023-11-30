package com.ssf.primaryhealthcare.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PHCCountContentResponse {

    private Long count;
    private Object content;

}
