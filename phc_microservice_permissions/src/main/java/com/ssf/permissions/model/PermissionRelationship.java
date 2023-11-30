package com.ssf.permissions.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class PermissionRelationship {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private RelationshipType type;
    private Map properties = new HashMap();
}
