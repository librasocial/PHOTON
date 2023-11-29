package com.ssf.childcareimmunization.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.childcareimmunization.constants.Constants;
import com.ssf.childcareimmunization.dtos.AuditDto;
import com.ssf.childcareimmunization.dtos.ReactionDto;
import com.ssf.childcareimmunization.dtos.VaccinesDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Document(collection = Constants.CHILD_CARE_IMMUNIZATION_SERVICE_COLLECTION_NAME)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Immunization {

	@Id
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
