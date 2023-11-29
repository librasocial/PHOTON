package com.ssf.permissions.dtos;


import com.ssf.permissions.model.NodeType;
import com.ssf.permissions.model.RelationshipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelationshipFilterDTO {

    private RelationshipType relationshipType;

    private Map<String, Object> targetProperties = new HashMap<>();

    private Map<String, Object> sourceProperties = new HashMap<>();

    private Map<String, Object> relationshipProperties = new HashMap<>();

    private Map<String, Object> targetInClause = new HashMap<>();

    private Map<String, Object> srcInClause = new HashMap<>();

    private Map<String, Object> relInClause = new HashMap<>();

    private NodeType sourceType;

    private NodeType targetType;

    private Integer stepCount = 1;

    private Integer page = 0;

    private Integer size = 5;

}
