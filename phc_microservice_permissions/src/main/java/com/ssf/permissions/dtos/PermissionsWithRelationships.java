package com.ssf.permissions.dtos;

import com.ssf.permissions.model.PermissionNode;
import com.ssf.permissions.model.PermissionRelationship;
import lombok.Data;

@Data
public class PermissionsWithRelationships {
    private PermissionNode sourceNode;
    private PermissionRelationship relationship;
    private PermissionNode targetNode;

}
