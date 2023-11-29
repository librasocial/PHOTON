package com.ssf.membership.dtos;

import com.ssf.membership.model.MemberType;
import com.ssf.membership.model.RelationshipType;
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
    private RelationshipType type;
    private MemberType srcType;
    private MemberType targetType;
}
