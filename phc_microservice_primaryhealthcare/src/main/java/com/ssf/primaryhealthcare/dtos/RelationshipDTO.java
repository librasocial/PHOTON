package com.ssf.primaryhealthcare.dtos;

import com.ssf.primaryhealthcare.model.RelationshipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RelationshipDTO {

    private RelationshipType type;

    private Map<String, Object> properties;

    private PHCNodeDTO source;

    private PHCNodeDTO target;

}
