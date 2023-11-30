package com.ssf.permissions.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"JuniorHealthAssistant", "Employee"})
@Data
public class JuniorHealthAssistant extends Employee {
}
