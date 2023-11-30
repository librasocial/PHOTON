package com.ssf.organization.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import lombok.Data;

@Data
@RelationshipProperties
public class CountryAndState {
	@Id
	@GeneratedValue
	private Long id;
	
	@TargetNode
	private  State state;

	public CountryAndState(State state) {
		this.state = state;
	}
	
	

}
