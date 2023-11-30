package com.ssf.permissions.controller;


import com.ssf.permissions.dtos.*;
import com.ssf.permissions.model.NodeType;
import com.ssf.permissions.model.RelationshipType;
import com.ssf.permissions.service.IPermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/permissions")
public class PermissionsController {

    @Autowired
    private IPermissionsService service;

    @PostMapping("")
    public ResponseEntity<PermissionPageResponse> createPermission(@RequestBody RelationshipDTO permissions) {
        return ResponseEntity.ok(service.createPermission(permissions));
    }

    @PostMapping("/assert")
    public ResponseEntity<Boolean> assertPermissions(@RequestBody PermissionAssetFilter assetFilter) {
        return ResponseEntity.ok(service.assertPermissions(assetFilter));
    }

    @GetMapping("/relationships/filter")
    public ResponseEntity<PermissionPageResponse> getMemberByFilterWithRelationships(@RequestParam(name = "rel") RelationshipType relationshipType,
                                                                                 @RequestParam(name = "srcType") Optional<NodeType> srcType,
                                                                                 @RequestParam(name = "srcNodeId") Optional<String> srcNodeId,
                                                                                 @RequestParam(name = "targetType") Optional<NodeType> targetType,
                                                                                 @RequestParam(name = "targetNodeId") Optional<String> targetNodeId,
                                                                                 @RequestParam(name = "page") Optional<Integer> page,
                                                                                 @RequestParam(name = "size") Optional<Integer> size,
                                                                                 @RequestParam(name = "stepCount") Optional<Integer> stepCount
    ) {
        PermissionFilterDTO dto = PermissionFilterDTO.builder().sourceId(srcNodeId.orElse(null)).targetType(targetType.orElse(null)).sourceType(srcType.orElse(null))
                .relationshipStepCount(stepCount.orElse(1)).relationshipType(relationshipType).targetId(targetNodeId.orElse(null)).build();
        return ResponseEntity.ok(service.getMemberAndRelationships(dto, page.orElse(0), size.orElse(25)));
    }

    @PostMapping("/relationships/filter")
    public ResponseEntity<PermissionPageResponse> getMemberByFilterWithRelationships(@RequestBody RelationshipFilterDTO filterInput) {
        return ResponseEntity.ok(service.getMemberRelationshipByFilters(filterInput));
    }


}
