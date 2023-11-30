package com.ssf.surveillance.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IodineSamplesFilterDTO {
    private Integer page;
    private Integer size;
    private String surveillanceId;
}
