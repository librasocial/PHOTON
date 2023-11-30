package com.ssf.permissions.dtos;

import com.ssf.permissions.model.NodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Member {
    @NotBlank
    private NodeType type;
    @NotBlank
    private Map<String, Object> properties;
}
