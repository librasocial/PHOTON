package com.ssf.organization.dtos;

import com.ssf.organization.model.OrgType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Organization {
    @NotBlank
    private OrgType type;
    @NotBlank
    private Map<String, Object> properties;

}
