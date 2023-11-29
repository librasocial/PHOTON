package com.ssf.organization.entities;


import com.ssf.organization.dtos.AuditDto;
import com.ssf.organization.model.AssetType;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import javax.validation.constraints.NotNull;
import java.util.List;

@Node
@Data
public class Place extends AuditDto implements Org {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String uuid;
    private String name;
    private AssetType assetType;
    private String assetSubType;
    private List<String> imgUrl;
    private Double latitude;
    private Double longitude;
    private String requiredSurveys;
    private Long typeCount;

}
