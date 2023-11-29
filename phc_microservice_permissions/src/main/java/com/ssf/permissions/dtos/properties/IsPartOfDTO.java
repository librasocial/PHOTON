package com.ssf.permissions.dtos.properties;

import com.ssf.permissions.dtos.AuditDto;
import lombok.Data;

@Data
public class IsPartOfDTO extends AuditDto {
    private String uuid;
}
