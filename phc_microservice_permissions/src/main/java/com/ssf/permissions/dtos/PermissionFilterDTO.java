package com.ssf.permissions.dtos;

import com.ssf.permissions.model.NodeType;
import com.ssf.permissions.model.RelationshipType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.w3c.dom.Node;

import java.util.Map;

@Data
@Builder
public class PermissionFilterDTO {
    private NodeType sourceType;
    private String value;
    private String key;
    private Pageable pageable;
    private RelationshipType relationshipType;
    private Map<String, String> attributes;
    private String sourceId;
    private String targetId;
    private NodeType targetType;
    private Integer relationshipStepCount;
}
