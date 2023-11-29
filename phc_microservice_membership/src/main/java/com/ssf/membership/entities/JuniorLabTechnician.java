package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"JuniorLabTechnician", "Employee"})
@Data
public class JuniorLabTechnician extends Employee implements IMemberEntity {
}
