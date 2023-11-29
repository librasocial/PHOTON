package com.ssf.laborders.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssf.laborders.entities.Sample;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderSamplesDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    private String orderId;

    private Sample sample;

}
