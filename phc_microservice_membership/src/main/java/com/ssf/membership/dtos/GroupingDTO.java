package com.ssf.membership.dtos;

import com.ssf.membership.model.RelationshipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupingDTO {
    private Member source;
    private Member target;
    private RelationshipType relationship;
    private String field;
    private Integer stepCount;
}
