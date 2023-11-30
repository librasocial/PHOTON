package com.ssf.membership.service;

import com.ssf.membership.dtos.*;
import com.ssf.membership.entities.Expression;
import com.ssf.membership.model.SearchType;

import java.util.Map;
import java.util.List;

public interface IMemberService {

    public MemberPageResponse getMemberById(String id);

    public Map<String, Object> createMember(MembersDTO request);

    public MemberPageResponse getMembers(MemberFilterDTO filter);

    public MemberPageResponse getRelationshipsByMemberById(String id, Integer page, Integer size);

    public MemberPageResponse getMemberAndRelationships(MemberFilterDTO filter, Integer page, Integer size);

    public Map<String, Object> updateMember(MembersDTO request, String id);

    public MemberPageResponse searchCitizenByFilters(String query, SearchType type, NodeFilterInput filterInput);

    public MemberPageResponse createRelationship(RelationshipDTO relationshipDTO);

    public MemberPageResponse getRelationshipsGrouping(GroupingDTO groupingDTO);

    public MemberPageResponse updateRelationship(String relationshipId, RelationshipPatchDTO relationshipPatchDTO);

    public MemberPageResponse getMemberRelationshipByFilters(RelationshipFilterDTO filterInput);

    public MemberPageResponse getMembersWithFilters(MemberFiltersDTO filtersDTO);
}
