package com.ssf.abdm.dto;

import com.ssf.abdm.constant.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestDTO {
    private String id;
    private RequestType type;
    private Integer code;
}
