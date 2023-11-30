package com.ssf.membership.controller;

import com.ssf.membership.dtos.*;
import com.ssf.membership.model.MemberType;
import com.ssf.membership.model.RelationshipType;
import com.ssf.membership.model.SearchType;
import com.ssf.membership.service.IMemberService;
import com.ssf.membership.service.IS3BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class MemberController {

    @Autowired
    private IMemberService service;
    @Autowired
    private IS3BucketService s3ConfigService;

    @GetMapping("/members/{memberId}")
    public ResponseEntity<MemberPageResponse> getMemberById(@PathVariable(name = "memberId") String id) {
        return ResponseEntity.ok(service.getMemberById(id));
    }

    @GetMapping("/members/{memberId}/relationships")
    public ResponseEntity<MemberPageResponse> getRelationshipsById(@PathVariable(name = "memberId") String id,
                                                                   @RequestParam(name = "page") Optional<Integer> page,
                                                                   @RequestParam(name = "size") Optional<Integer> size) {
        return ResponseEntity.ok(service.getRelationshipsByMemberById(id, page.orElse(0), size.orElse(25)));
    }

    @PostMapping("/members")
    public ResponseEntity<Map<String, Object>> createMember(@RequestBody MembersDTO request) {
        return ResponseEntity.ok(service.createMember(request));
    }

    @GetMapping("/members/filter")
    public ResponseEntity<MemberPageResponse> getMemberByFilterValues(@RequestParam(name = "type") MemberType type,
                                                                      @RequestParam(name = "value") Optional<String> value,
                                                                      @RequestParam(name = "id") Optional<String> id,
                                                                      @RequestParam(name = "key") Optional<String> key,
                                                                      @RequestParam(name = "page") Optional<Integer> page,
                                                                      @RequestParam(name = "size") Optional<Integer> size) {
        Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(25));
        MemberFilterDTO dto = MemberFilterDTO.builder().sourceType(type).pageable(pageable).build();
        if (key.isPresent() && value.isPresent()) {
            Map<String, Object> filterMapToBuildQuery = new HashMap<>();
            filterMapToBuildQuery.put(key.get(), value.get());
            dto.setAttributes(filterMapToBuildQuery);
        }
        return ResponseEntity.ok(service.getMembers(dto));
    }

    @GetMapping("/members/relationships/filter")
    public ResponseEntity<MemberPageResponse> getMemberByFilterWithRelationships(@RequestParam(name = "rel") RelationshipType relationshipType,
                                                                                 @RequestParam(name = "srcType") Optional<MemberType> srcType,
                                                                                 @RequestParam(name = "srcNodeId") Optional<String> srcNodeId,
                                                                                 @RequestParam(name = "targetType") Optional<MemberType> targetType,
                                                                                 @RequestParam(name = "targetNodeId") Optional<String> targetNodeId,
                                                                                 @RequestParam(name = "page") Optional<Integer> page,
                                                                                 @RequestParam(name = "size") Optional<Integer> size,
                                                                                 @RequestParam(name = "stepCount") Optional<Integer> stepCount
    ) {
        MemberFilterDTO dto = MemberFilterDTO.builder().sourceId(srcNodeId.orElse(null)).targetType(targetType.orElse(null)).sourceType(srcType.orElse(null))
                .relationshipStepCount(stepCount.orElse(1)).relationshipType(relationshipType).targetId(targetNodeId.orElse(null)).build();
        return ResponseEntity.ok(service.getMemberAndRelationships(dto, page.orElse(0), size.orElse(25)));
    }

    @PostMapping("/members/relationships/filter")
    public ResponseEntity<MemberPageResponse> getMemberByFilterWithRelationships(@RequestBody RelationshipFilterDTO filterInput) {
        return ResponseEntity.ok(service.getMemberRelationshipByFilters(filterInput));
    }

    @PatchMapping("/members/{memberId}")
    public ResponseEntity<Map<String, Object>> updateMember(@PathVariable(name = "memberId") String id, @RequestBody MembersDTO request) {
        return ResponseEntity.ok(service.updateMember(request, id));
    }


    @PostMapping("/members/search")
    public ResponseEntity<MemberPageResponse> searchByFilters(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "type") SearchType type,
            @RequestBody NodeFilterInput filterInput) {
        return ResponseEntity.ok(service.searchCitizenByFilters(query, type, filterInput));
    }

    @GetMapping("/members/getprefetchedurl")
    public ResponseEntity<MemberS3PresignedUrl> getPreFetchedURL(
            @RequestParam(name = "bucketKey") String bucketKey,
            @RequestParam(name = "assetType", required = false, defaultValue = "Members-assets") String assetType) {
        return ResponseEntity.ok(s3ConfigService.signBucket(assetType, bucketKey));
    }

    @GetMapping("/members/getimageurl")
    public ResponseEntity<MemberS3PresignedUrl> getImageURL(
            @RequestParam(name = "bucketKey") String bucketKey,
            @RequestParam(name = "assetType", required = false, defaultValue = "Members-assets") String assetType) {
        return ResponseEntity.ok(s3ConfigService.getImageUrl(assetType, bucketKey));
    }

    @PostMapping("/members/{memberId}/relationships")
    public ResponseEntity<MemberPageResponse> createRelationship(
            @PathVariable(name = "memberId") String id,
            @RequestBody RelationshipDTO relationshipDTO) {
        return ResponseEntity.ok(service.createRelationship(relationshipDTO));
    }

    @PostMapping("/members/relationships/grouping")
    public ResponseEntity<MemberPageResponse> getRelationshipsGrouping(@RequestBody GroupingDTO groupingDTO) {
        return ResponseEntity.ok(service.getRelationshipsGrouping(groupingDTO));
    }

    @PatchMapping("/members/relationships/{relationshipId}")
    public ResponseEntity<MemberPageResponse> getRelationshipsGrouping(@PathVariable(name = "relationshipId") String relationshipId,
                                                                       @RequestBody RelationshipPatchDTO relationshipPatchDTO) {
        return ResponseEntity.ok(service.updateRelationship(relationshipId, relationshipPatchDTO));
    }

    @PostMapping("/members/filter")
    public ResponseEntity<MemberPageResponse> getMemberByFilterValues(@RequestBody MemberFiltersDTO filtersDTO) {
        return ResponseEntity.ok(service.getMembersWithFilters(filtersDTO));
    }


    @GetMapping("/members/health")
    public ResponseEntity<String> getHealth() {
        return ResponseEntity.ok("Working....");
    }
}
