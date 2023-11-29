package com.ssf.eligiblecouple.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.ssf.eligiblecouple.constant.EligibleCoupleConst;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = EligibleCoupleConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime dateCreated;
    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = EligibleCoupleConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime dateModified;
}
