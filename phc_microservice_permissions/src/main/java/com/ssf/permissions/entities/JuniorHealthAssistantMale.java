package com.ssf.permissions.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"JuniorHealthAssistantMale", "Employee"})
@Data
public class JuniorHealthAssistantMale extends Employee {
}
