package com.ssf.primaryhealthcare.model;

import lombok.Data;

import java.util.Map;

@Data
public class PHCRelationship {

    private Long id;

    private RelationshipType type;

    private Map properties;
}
