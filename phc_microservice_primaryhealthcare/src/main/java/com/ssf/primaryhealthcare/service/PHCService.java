package com.ssf.primaryhealthcare.service;

import com.ssf.primaryhealthcare.dtos.*;
import com.ssf.primaryhealthcare.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.config.EnableNeo4jAuditing;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@EnableNeo4jAuditing
public class PHCService implements IPHCService {


    @Autowired
    HttpServletRequest request;

    @Autowired
    private PHCRelationshipRepository PHCRelationshipRepository;
    @Override
    public PHCPageResponse createRelationship(RelationshipDTO relationshipDTO) {
        return PHCPageResponse.builder().totalPages(1L).totalElements(1L).content(PHCRelationshipRepository.createRelationshipBetweenNodes(relationshipDTO)).build();
    }

    @Override
    public PHCPageResponse updateRelationship(String relationshipId, RelationshipPatchDTO relationshipPatchDTO) {
        List content = PHCRelationshipRepository.updatedRelationshipProperties(relationshipId, relationshipPatchDTO);
        return PHCPageResponse.builder().totalPages(1L).totalElements(Long.valueOf(content.size())).content(content).build();
    }

}
