package com.ssf.laborders.entities;

import com.ssf.laborders.constants.Constant;
import com.ssf.laborders.dtos.AuditDTO;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = Constant.TEST_RESULTS_COLLECTION)
public class TestResults {

    @Id
    private String id;

    private String orderId;

    private String orderSampleId;

    private Sample sample;

    private List<Result> results;

    private AuditDTO audit;
}
