package com.ssf.organization.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrgRelationshipFilterDTO {
	private String srcId;
	private String trgId;
	private String rel;
	

}
