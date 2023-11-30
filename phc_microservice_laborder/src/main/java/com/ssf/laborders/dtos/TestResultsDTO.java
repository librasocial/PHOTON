package com.ssf.laborders.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssf.laborders.entities.Sample;
import com.ssf.laborders.entities.Result;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestResultsDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    private String orderId;

    private String orderSampleId;

    private Sample sample;

    private List<Result> results;
}
