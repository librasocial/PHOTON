package com.ssf.organization.dtos;

import com.ssf.organization.model.RelType;
import lombok.Data;

@Data
public class GroupingDTO {
    private Organization source;
    private Organization target;
    private RelType relationship;
    private String field;
    private Integer stepCount;
}
