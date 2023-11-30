package com.ssf.organization.dtos;

import com.ssf.organization.model.OrgType;
import com.ssf.organization.model.RelType;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelationshipFilterDTO {

    private RelType relationshipType;

    private Map<String, Object> targetProperties = new HashMap<>();

    private Map<String, Object> sourceProperties = new HashMap<>();

    private Map<String, Object> relationshipProperties = new HashMap<>();

    private Map<String, Object> targetInClause = new HashMap<>();

    private Map<String, Object> srcInClause = new HashMap<>();

    private Map<String, Object> relInClause = new HashMap<>();

    private List<Expression> srcExpressions = new ArrayList<>();

    private List<Expression> targetExpressions = new ArrayList<>();

    private List<Expression> relExpressions = new ArrayList<>();

    private OrgType targetType;

    private OrgType sourceType;

    private Integer stepCount = 1;

    private Integer page = 0;

    private Integer size = 25;

}
