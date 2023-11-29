package com.ssf.primaryhealthcare.service;

import com.ssf.primaryhealthcare.dtos.PHCPageResponse;
import com.ssf.primaryhealthcare.dtos.RelationshipDTO;
import com.ssf.primaryhealthcare.dtos.RelationshipPatchDTO;

public interface IPHCService {

    public PHCPageResponse createRelationship(RelationshipDTO relationshipDTO);

    public PHCPageResponse updateRelationship(String relationshipId, RelationshipPatchDTO relationshipPatchDTO);

}
