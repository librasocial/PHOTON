package com.ssf.membership.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

@Node(labels = {"JuniorParaMedicalOphthalmicAssistant", "Employee"})
@Data
public class JuniorParaMedicalOphthalmicAssistant extends Employee implements IMemberEntity {
}
