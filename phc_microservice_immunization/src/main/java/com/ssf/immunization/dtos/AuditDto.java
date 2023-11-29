package com.ssf.immunization.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.immunization.constant.ImmunizationConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public  class AuditDto {
    @CreatedBy
    @NotNull
    private String createdBy;
    @LastModifiedBy
    @NotNull
    private String modifiedBy;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ImmunizationConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime dateCreated;
    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ImmunizationConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime dateModified;
}
