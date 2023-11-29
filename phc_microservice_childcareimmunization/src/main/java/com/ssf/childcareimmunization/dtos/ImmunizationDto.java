package com.ssf.childcareimmunization.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssf.childcareimmunization.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImmunizationDto {
	private String id;
	private String childCitizenId;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = Constants.ISO_DATE_FORMAT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_FORMAT)
	private LocalDate registrationDate;
	private String ashaWorker;
	private String targetDiseaseName;
	private String description;
	private String series;
	private String doseNumber;
	private String aefiStatus;
	private String aefiReason;
	private List<VaccinesDto> vaccines;
	private ReactionDto reaction;
	private Boolean isCovidTestDone;
	private Boolean isCovidResultPositive;
	private Boolean isILIExperienced;
	private Boolean didContactCovidPatient;
	private AuditDto audit;

}
