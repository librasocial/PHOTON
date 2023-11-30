package com.ssf.permissions.dtos;

import com.ssf.permissions.model.PermissionRelationship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RelationshipDTO {

    private PermissionRelationship relationship;

    private Member source;

    private Resource target;

}
