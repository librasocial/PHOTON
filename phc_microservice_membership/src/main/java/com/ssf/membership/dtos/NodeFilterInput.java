package com.ssf.membership.dtos;

import com.ssf.membership.model.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeFilterInput {

    private MemberType type;
    private Integer page;
    private Integer size;
    private Map<String, Object> properties;

}
