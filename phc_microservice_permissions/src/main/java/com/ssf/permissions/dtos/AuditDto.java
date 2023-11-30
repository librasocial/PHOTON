package com.ssf.permissions.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.permissions.utils.PermissionConstants;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public abstract class AuditDto {
    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = PermissionConstants.ISO_DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PermissionConstants.ISO_DATE_TIME)
    private LocalDateTime dateCreated;
    private String createdBy;
    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = PermissionConstants.ISO_DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PermissionConstants.ISO_DATE_TIME)
    private LocalDateTime dateModified;
    private String modifiedBy;
}
