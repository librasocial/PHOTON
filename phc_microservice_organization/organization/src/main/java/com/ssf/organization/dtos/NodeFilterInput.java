package com.ssf.organization.dtos;

import com.ssf.organization.model.OrgType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeFilterInput {

    private OrgType type;
    private Integer page;
    private Integer size;
    private Map<String, Object> properties;
    private List<Expression> expressions;

}
