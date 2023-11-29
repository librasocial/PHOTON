package com.ssf.organization.model;

import lombok.Data;

import java.util.Map;

@Data
public class OrgRelationship {

    private Long id;

    private RelType type;

    private Map properties;
}
