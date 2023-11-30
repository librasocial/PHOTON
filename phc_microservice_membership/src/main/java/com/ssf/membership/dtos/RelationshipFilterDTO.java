package com.ssf.membership.dtos;

import com.ssf.membership.entities.Expression;
import com.ssf.membership.model.MemberType;
import com.ssf.membership.model.RelationshipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private List<Expression> srcExpressions = new ArrayList<>();

    private List<Expression> targetExpressions = new ArrayList<>();

    private List<Expression> relExpressions = new ArrayList<>();

    private MemberType sourceType;

    private MemberType targetType;

    private Integer stepCount = 1;

    private Integer page = 0;

    private Integer size = 25;

}
