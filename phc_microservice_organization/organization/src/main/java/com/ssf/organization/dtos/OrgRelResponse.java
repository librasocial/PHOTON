package com.ssf.organization.dtos;

import com.ssf.organization.model.OrgNode;
import com.ssf.organization.model.OrgRelationship;
import lombok.Data;

@Data
public class OrgRelResponse {
    private OrgNode source;
    private OrgNode target;
    private OrgRelationship rel;

}
