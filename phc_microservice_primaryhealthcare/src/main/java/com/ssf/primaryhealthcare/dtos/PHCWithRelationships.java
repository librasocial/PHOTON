package com.ssf.primaryhealthcare.dtos;

import com.ssf.primaryhealthcare.model.PHCNode;
import com.ssf.primaryhealthcare.model.PHCRelationship;
import lombok.Data;

@Data
public class PHCWithRelationships {
    private PHCNode sourceNode;
    private PHCRelationship relationship;
    private PHCNode targetNode;

}
