package com.ssf.childcareimmunization.dtos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssf.childcareimmunization.constants.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendationsDto {
	private String id;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = Constants.ISO_DATE_FORMAT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_FORMAT)
	private LocalDate registrationDate;
	private String ashaWorker;
	private String childCitizenId;
	private List<RecommendationsDetailsDto> recommendations;
	private AuditDto audit;

}
