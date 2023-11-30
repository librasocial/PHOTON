package com.ssf.membership.dtos;

import com.ssf.membership.model.MemberNode;
import com.ssf.membership.model.MemberRelationship;
import com.ssf.membership.model.RelationshipType;
import lombok.Data;
import org.neo4j.driver.types.Relationship;

@Data
public class MemberWithRelationships{
    private MemberNode SourceNode;
    private MemberRelationship relationship;
    private MemberNode TargetNode;

}
