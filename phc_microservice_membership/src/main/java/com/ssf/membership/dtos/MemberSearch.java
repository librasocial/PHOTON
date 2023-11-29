package com.ssf.membership.dtos;

import com.ssf.membership.model.MemberNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSearch {

    private MemberNode node;
    private Double score;

}
