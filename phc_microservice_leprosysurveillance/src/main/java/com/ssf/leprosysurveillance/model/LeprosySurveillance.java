package com.ssf.leprosysurveillance.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.leprosysurveillance.constants.Constants;
import com.ssf.leprosysurveillance.dto.AuditDto;
import com.ssf.leprosysurveillance.dto.LabResultDto;
import com.ssf.leprosysurveillance.dto.PastDto;
import com.ssf.leprosysurveillance.dto.SampleDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Document(collection = Constants.LEPROSY_SURVEILLANCE_SERVICE_COLLECTION_NAME)
@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class LeprosySurveillance extends AuditDto{

	@Id
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

}