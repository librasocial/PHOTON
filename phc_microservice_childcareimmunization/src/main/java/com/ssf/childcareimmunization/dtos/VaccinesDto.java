package com.ssf.childcareimmunization.dtos;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.childcareimmunization.constants.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = Constants.CHILD_CARE_RECOMMENDATION_SERVICE_COLLECTION_NAME)
public class VaccinesDto {
	private String code;
	private String name;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = Constants.ISO_DATE_FORMAT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_FORMAT)
	private LocalDate earliestDueDate;
	private String pendingReason;
	private ImmunizationDetailDto immunization;
}
