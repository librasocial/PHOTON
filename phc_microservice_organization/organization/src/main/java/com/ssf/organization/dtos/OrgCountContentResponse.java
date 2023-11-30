package com.ssf.organization.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrgCountContentResponse {

    private Long count;
    private Object content;

}
