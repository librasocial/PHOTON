package com.ssf.organization.dtos;

import com.ssf.organization.model.RelType;
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
    private RelType type;
    private Map<String, Object> properties;
}
