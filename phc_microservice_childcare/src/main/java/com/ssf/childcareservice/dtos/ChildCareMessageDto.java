package com.ssf.childcareservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChildCareMessageDto {
    private String authorization;
    private String idToken;
    private ChildCareDto payload;
}
