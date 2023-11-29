package com.ssf.tbsurveillance.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.tbsurveillance.constants.Constants;
import com.ssf.tbsurveillance.dtos.AuditDto;
import com.ssf.tbsurveillance.dtos.LabResultDto;
import com.ssf.tbsurveillance.dtos.SampleDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = Constants.TB_SURVEILLANCE_COLLECTION)
@NoArgsConstructor
public class TbSurveillance  extends AuditDto {
	@Id
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
}
