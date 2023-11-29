package com.ssf.childcareimmunization.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.childcareimmunization.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ImmunizationSubGroup {
	private String name;
	private String batchNumber;
	private String manufacturer;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = Constants.ISO_DATE_FORMAT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_FORMAT)
	private LocalDate expirationDate;
	private String ashaWorker;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = Constants.ISO_DATE_FORMAT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_FORMAT)
	private LocalDate vaccinatedDate;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = Constants.ISO_DATE_FORMAT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_FORMAT)
	private LocalDate earliestDueDate;
	private String pendingReason;
	private String aefiStatus;
	private String aefiReason;
	private Boolean isCovidResultPositive;

}
