package com.ssf.laborders.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenderCountDTO {
    @Id
    private String id;
    private Long count;
}
