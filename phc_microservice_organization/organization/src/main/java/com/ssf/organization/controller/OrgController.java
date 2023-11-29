package com.ssf.organization.controller;

import com.ssf.organization.dtos.*;
import com.ssf.organization.entities.Org;
import com.ssf.organization.model.OrgType;
import com.ssf.organization.model.RelType;
import com.ssf.organization.service.IOrgService;
import com.ssf.organization.service.IS3BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
//@Validated
public class OrgController {

    @Autowired
    private IOrgService service;

    @Autowired
    private IS3BucketService s3ConfigService;

    @GetMapping("/PHCs")
    public ResponseEntity<OrgPageResponse> getAllPhc(
            @RequestParam(name = "page") Optional<Integer> page,
            @RequestParam(name = "size") Optional<Integer> size
    ) {
        return ResponseEntity.ok(service.getAllPhcs(page.orElse(0), size.orElse(25)));

    }

    @PostMapping("/organizations")
    public ResponseEntity<Org> createOrg(@RequestBody OrganizationDTO req) {
        return ResponseEntity.ok(service.createOrganization(req));
    }

    @GetMapping("/organizations/{organizationId}")
    public ResponseEntity<OrganizationDTO> getOrganisationById(@PathVariable(name = "organizationId") String id) {
        return ResponseEntity.ok(service.getOrgById(id));
    }

    @PostMapping("/organizations/filter")
    public ResponseEntity<OrgPageResponse> getOrganisationById(@RequestBody NodeFilterInput filterInput) {
        return ResponseEntity.ok(service.getOrgsByFilters(filterInput));
    }

    @GetMapping("/organizations/relationships/filter")
    public ResponseEntity<OrgPageResponse> getOrganisationById(@RequestParam(name = "rel") RelType rel,
                                                               @RequestParam(name = "srcType") Optional<OrgType> srcType,
                                                               @RequestParam(name = "srcNodeId") Optional<String> srcNodeId,
                                                               @RequestParam(name = "targetType") Optional<OrgType> targetType,
                                                               @RequestParam(name = "targetNodeId") Optional<String> targetNodeId,
                                                               @RequestParam(name = "page") Optional<Integer> page,
                                                               @RequestParam(name = "size") Optional<Integer> size
    ) {
        OrgFilterDTO dto = OrgFilterDTO.builder().sourceId(srcNodeId.orElse(null)).targetType(targetType.orElse(null)).sourceType(srcType.orElse(null)).targetId(targetNodeId.orElse(null))
                .relType(rel).build();

        return ResponseEntity.ok(service.getOrgsAndRelationship(dto, page.orElse(0), size.orElse(25)));
    }

    @PostMapping("/organizations/relationships/filter")
    public ResponseEntity<OrgPageResponse> getOrganisationRelationshipByFilter(@RequestBody RelationshipFilterDTO filterInput) {
        return ResponseEntity.ok(service.getOrganisationRelationshipByFilter(filterInput));
    }

    @PatchMapping("/organizations/{organizationId}")
    public ResponseEntity<Org> updateOrg(@PathVariable(name = "organizationId") String id, @RequestBody OrganizationDTO req) {
        return ResponseEntity.ok(service.updateOrg(req, id));
    }

    @PostMapping("/organizations/nearme")
    public ResponseEntity<OrgPageResponse> getNearMeOrg(@RequestBody NodeFilterInput filterInput) {

        return ResponseEntity.ok(service.getNearMeOrg(filterInput));
    }

    @PostMapping("/organizations/grouping")
    public ResponseEntity<OrgPageResponse> getGroupingCounts(@RequestBody NodeFilterInput filterInput) {

        return ResponseEntity.ok(service.getPlacesCount(filterInput));
    }

    @GetMapping("/organizations/getprefetchedurl")
    public ResponseEntity<OrgS3PresignedUrl> getPreFetchedURL(
            @RequestParam(name = "bucketKey") String bucketKey,
            @RequestParam(name = "assetType", required = false, defaultValue = "Organizations-assets") String assetType) {
        return ResponseEntity.ok(s3ConfigService.signBucket(assetType, bucketKey));
    }

    @GetMapping("/organizations/getimageurl")
    public ResponseEntity<OrgS3PresignedUrl> getImageURL(
            @RequestParam(name = "bucketKey") String bucketKey,
            @RequestParam(name = "assetType", required = false, defaultValue = "Organizations-assets") String assetType) {
        return ResponseEntity.ok(s3ConfigService.getImageUrl(assetType, bucketKey));
    }


    @PostMapping("/organizations/search")
    public ResponseEntity<OrgPageResponse> searchByFilters(
            @RequestParam(name = "query") String query,
            @RequestBody NodeFilterInput filterInput) {
        return ResponseEntity.ok(service.searchHouseHoldByFilters(query, filterInput));
    }

    @PostMapping("/organizations/relationships/grouping")
    public ResponseEntity<OrgPageResponse> getRelationshipsGrouping(@RequestBody GroupingDTO groupingDTO) {
        return ResponseEntity.ok(service.getRelationshipsGrouping(groupingDTO));
    }

    @PostMapping("/organizations/relationships")
    public ResponseEntity<OrgPageResponse> createRelationship(
            @RequestBody RelationshipDTO relationshipDTO) {
        return ResponseEntity.ok(service.createRelationship(relationshipDTO));
    }

    @PatchMapping("/organizations/relationships/{relationshipId}")
    public ResponseEntity<OrgPageResponse> getRelationshipsGrouping(@PathVariable(name = "relationshipId") String relationshipId,
                                                                       @RequestBody RelationshipPatchDTO relationshipPatchDTO) {
        return ResponseEntity.ok(service.updateRelationship(relationshipId, relationshipPatchDTO));
    }

}
