package com.ssf.organization.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.Data;

@Data
public class OrgNode {
	private long id;
	private List<String> labels;
	private Map<String,Object> properties;
;
}
