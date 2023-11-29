package com.ssf.primaryhealthcare.dtos;

import com.ssf.primaryhealthcare.model.NodeType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PHCNodeDTO {
    @NotBlank
    private NodeType type;
    @NotBlank
    private Map<String,Object> properties;
}
