package com.ssf.organization.service;

import com.ssf.organization.dtos.*;
import com.ssf.organization.entities.Org;

public interface IOrgService {

    public OrgPageResponse getAllPhcs(Integer page, Integer size);

    public Org createOrganization(OrganizationDTO req);

    public Org updateOrg(OrganizationDTO req, String id);

    public OrganizationDTO getOrgById(String id);

    public OrgPageResponse getOrgs(OrgFilterDTO filter);

    public OrgPageResponse getOrgsAndRelationship(OrgFilterDTO filter, Integer page, Integer size);

    public OrgPageResponse getNearMeOrg(NodeFilterInput filterInput);

    public OrgPageResponse getPlacesCount(NodeFilterInput filterInput);

    public OrgPageResponse getOrgsByFilters(NodeFilterInput filterInput);

    public OrgPageResponse searchHouseHoldByFilters(String query, NodeFilterInput filterInput);

    public OrgPageResponse getRelationshipsGrouping(GroupingDTO groupingDTO);

    public OrgPageResponse getOrganisationRelationshipByFilter(RelationshipFilterDTO filterInput);

    public OrgPageResponse createRelationship(RelationshipDTO relationshipDTO);

    public OrgPageResponse updateRelationship(String relationshipId, RelationshipPatchDTO relationshipPatchDTO);
}
