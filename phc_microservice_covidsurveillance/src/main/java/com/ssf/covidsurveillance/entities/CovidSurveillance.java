package com.ssf.covidsurveillance.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.covidsurveillance.constants.Constants;
import com.ssf.covidsurveillance.dtos.AuditDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Document(collection = Constants.COVID_SURVEILLANCE_COLLECTION)
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CovidSurveillance  extends AuditDto{
	@Id
	private String id;
	private String citizenId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
	private LocalDateTime dateOfSurveillance;
	private String vaccineType;
	private String noOfDoses;
	private boolean wasPreviouslyDiagnosed;

}
