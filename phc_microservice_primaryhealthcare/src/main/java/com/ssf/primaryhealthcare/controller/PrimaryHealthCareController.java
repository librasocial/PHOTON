package com.ssf.primaryhealthcare.controller;

import com.ssf.primaryhealthcare.dtos.PHCPageResponse;
import com.ssf.primaryhealthcare.dtos.RelationshipDTO;
import com.ssf.primaryhealthcare.dtos.RelationshipPatchDTO;
import com.ssf.primaryhealthcare.service.IPHCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PrimaryHealthCareController {

    @Autowired
    private IPHCService service;

    @PostMapping("/primaryhealthcare/relationship")
    public ResponseEntity<PHCPageResponse> createRelationship(@RequestBody RelationshipDTO relationshipDTO) {
        return ResponseEntity.ok(service.createRelationship(relationshipDTO));
    }

    @PatchMapping("/primaryhealthcare/relationship/{relationshipId}")
    public ResponseEntity<PHCPageResponse> getRelationshipsGrouping(@PathVariable(name = "relationshipId") String relationshipId,
                                                                    @RequestBody RelationshipPatchDTO relationshipPatchDTO) {
        return ResponseEntity.ok(service.updateRelationship(relationshipId, relationshipPatchDTO));
    }

}
