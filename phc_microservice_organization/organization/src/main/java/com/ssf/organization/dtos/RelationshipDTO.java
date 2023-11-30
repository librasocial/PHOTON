package com.ssf.organization.dtos;

import com.ssf.organization.model.RelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RelationshipDTO {

    private RelType type;

    private Map<String, Object> properties;

    private Organization source;

    private Organization target;

}
