package com.ssf.organization.dtos;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.ssf.organization.model.OrgType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDTO {
	@NotBlank
	private OrgType type;
	@NotBlank
	private Map<String,Object> properties;
}
