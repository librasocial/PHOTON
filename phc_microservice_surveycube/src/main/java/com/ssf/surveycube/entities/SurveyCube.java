package com.ssf.surveycube.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ssf.surveycube.constant.SurveyCubeConstant;
import com.ssf.surveycube.dtos.AuditDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = SurveyCubeConstant.SURVEY_CUBE_COLLECTION_NAME)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SurveyCube {
    @Id
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    private Context context;
    private Map<String, List<Object>> ses;
    private Map<String, List<Object>> ncd;
    private Map<String, List<Object>> emrPateint;
    private AuditDto audit;

}

