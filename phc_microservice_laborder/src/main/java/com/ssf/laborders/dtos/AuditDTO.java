package com.ssf.laborders.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.laborders.constants.Constant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditDTO {
    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_TIME_FORMAT)
    private LocalDateTime dateCreated;
    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_TIME_FORMAT)
    private LocalDateTime dateModified;
}
