package com.ssf.membership.dtos;

import com.ssf.membership.model.MemberType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MembersDTO {
    @NotBlank
    private MemberType type;
    @NotBlank
    private Map<String, Object> properties;
}
