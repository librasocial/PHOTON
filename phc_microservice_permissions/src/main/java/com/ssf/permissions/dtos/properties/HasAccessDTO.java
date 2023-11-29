package com.ssf.permissions.dtos.properties;

import com.ssf.permissions.dtos.AuditDto;
import lombok.Data;

@Data
public class HasAccessDTO  extends AuditDto {
    private String uuid;
    private String orgType;
    private String orgUuid;
}
