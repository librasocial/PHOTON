package com.ssf.permissions.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.permissions.dtos.*;
import com.ssf.permissions.repository.PermissionsRelationshipRepository;
import com.ssf.permissions.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.config.EnableNeo4jAuditing;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@EnableNeo4jAuditing
public class PermissionsService implements IPermissionsService {
    ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

    @Autowired
    HttpServletRequest request;

    @Autowired
    private PermissionsRelationshipRepository permissionsRelationshipRepository;

    @Override
    public PermissionPageResponse createPermission(RelationshipDTO permissions) {
        return PermissionPageResponse.builder().content(permissionsRelationshipRepository.createRelationshipBetweenNodes(permissions))
                .totalElements(1L).totalPages(1L).build();
    }

    @Override
    public Boolean assertPermissions(PermissionAssetFilter assetFilter) {
        return permissionsRelationshipRepository.assertPermission(assetFilter);
    }

    @Override
    public PermissionPageResponse getMemberAndRelationships(PermissionFilterDTO filter, Integer page, Integer size) {

        return permissionsRelationshipRepository.getMemberAndRelationships(filter, page, size);
    }

    @Override
    public PermissionPageResponse getMemberRelationshipByFilters(RelationshipFilterDTO filterInput) {
        PermissionCountContentResponse response = permissionsRelationshipRepository.getMemberRelationshipByFilters(filterInput);
        return PermissionPageResponse.builder().totalPages(Utils.getPagesByElementsAndSize(response.getCount(), filterInput.getSize())).totalElements(response.getCount()).content(response.getContent()).build();
    }
}
