package com.ssf.organization.dtos;

import com.ssf.organization.model.OrgType;
import com.ssf.organization.model.RelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRelationshipDTO {
    private String srcId;
    private String targetId;
    private RelType type;
    private OrgType srcType;
    private OrgType targetType;
}
