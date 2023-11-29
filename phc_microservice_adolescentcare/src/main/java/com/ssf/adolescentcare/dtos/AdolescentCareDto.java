package com.ssf.adolescentcare.dtos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssf.adolescentcare.constants.Constants;
import com.ssf.adolescentcare.constants.DataEntryStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class AdolescentCareDto {
	private String id;
	private String citizenId;
	private String rchId;
	private String registeredBy;
	private String registeredByName;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = Constants.ISO_DATE_TIME_FORMAT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
	private LocalDateTime registeredOn;
	private Integer financialYear;
	private String fatherName;
	private String mobileNumber;
	private String mobileOwner;
	private String birthCertificateNo;
	private String childAadharNo;
	private Boolean isCovidTestDone;
	private Boolean isCovidResultPositive;
	private Boolean isILIExperienced;
	private Boolean didContactCovidPatient;
	private DataEntryStatus dataEntryStatus;
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private AuditDto audit;

}
