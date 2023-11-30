package com.ssf.membership.dtos;

import com.ssf.membership.model.MemberType;
import com.ssf.membership.model.RelationshipType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class MemberFilterDTO {
    private MemberType sourceType;
    private String value;
    private String key;
    private Pageable pageable;
    private RelationshipType relationshipType;
    private Map<String,Object> attributes = new HashMap<>();
    private String sourceId;
    private String targetId;
    private MemberType targetType;
    private Integer relationshipStepCount;
}
