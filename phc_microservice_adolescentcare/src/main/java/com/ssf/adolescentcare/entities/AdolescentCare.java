package com.ssf.adolescentcare.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.adolescentcare.constants.Constants;
import com.ssf.adolescentcare.constants.DataEntryStatus;
import com.ssf.adolescentcare.dtos.AuditDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document(collection = Constants.ADOLESCENT_CARE_COLLECTION_NAME)
@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AdolescentCare extends AuditDto {
	@Id
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
}
