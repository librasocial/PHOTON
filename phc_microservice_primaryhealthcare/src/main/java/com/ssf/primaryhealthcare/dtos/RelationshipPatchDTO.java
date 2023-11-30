package com.ssf.primaryhealthcare.dtos;

import com.ssf.primaryhealthcare.model.RelationshipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipPatchDTO {
    private RelationshipType type;
    private Map<String, Object> properties;
}
