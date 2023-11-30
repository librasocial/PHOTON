package com.ssf.membership.model;

import lombok.Data;

import java.util.Map;

@Data
public class MemberRelationship {

    private Long id;

    private RelationshipType type;

    private Map properties;
}
