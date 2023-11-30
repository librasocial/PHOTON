package com.ssf.membership.dtos;

import com.ssf.membership.entities.Expression;
import com.ssf.membership.model.MemberType;
import com.ssf.membership.model.RelationshipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberFiltersDTO {
    private MemberType type;
    private List<Expression> expressions;
    private Integer page = 0;
    private Integer size = 25;
}
