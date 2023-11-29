package com.ssf.organization.dtos;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.ssf.organization.model.OrgType;
import com.ssf.organization.model.RelType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrgFilterDTO {
	
	private OrgType type;
	private OrgType targetType;
	private OrgType sourceType;
	private String value;
	private String sourceId;
	private String targetId;
	private RelType relType;
	private Pageable pageable;
	private Map<String,String> attributes;

}
