package com.ssf.tbsurveillance.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.tbsurveillance.constants.Constants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class TbSurveillanceDto {
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private String id;
	private String citizenId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
	private LocalDateTime dateOfSurveillance;
	private Boolean wasTreatedForTBInPast;
	private Boolean isReferredToPhc;
	private Boolean hasDiabetes;
	private Boolean hasTBSymptoms;
	private String tbSymptom;
	private List<SampleDto> sample;
	private LabResultDto labResult;
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private AuditDto audit;
}


