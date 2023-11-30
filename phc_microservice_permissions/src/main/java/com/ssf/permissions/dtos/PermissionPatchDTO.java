package com.ssf.permissions.dtos;

import com.ssf.permissions.model.RelationshipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionPatchDTO {
    private RelationshipType type;
    private Map<String, Object> properties;
}
