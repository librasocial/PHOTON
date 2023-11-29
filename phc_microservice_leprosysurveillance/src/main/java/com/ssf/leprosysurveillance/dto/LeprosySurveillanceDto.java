package com.ssf.leprosysurveillance.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssf.leprosysurveillance.constants.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeprosySurveillanceDto {
	private String id;
	private String citizenId;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = Constants.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
	private LocalDateTime dateOfSurveillance;
	private Boolean isNewlySuspected;
	private String suspectedType;
	private List<String> symptoms;
	private Boolean isReferredToPhc;
	private PastDto past;
	private SampleDto sample;
	private LabResultDto labResult;
	private AuditDto audit;

}
