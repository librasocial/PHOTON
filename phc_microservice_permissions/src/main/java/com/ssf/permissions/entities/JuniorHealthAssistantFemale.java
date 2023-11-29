package com.ssf.permissions.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"JuniorHealthAssistantFemale", "Employee"})
@Data
public class JuniorHealthAssistantFemale extends Employee {
}
