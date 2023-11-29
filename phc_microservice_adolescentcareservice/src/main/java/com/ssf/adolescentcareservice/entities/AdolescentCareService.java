package com.ssf.adolescentcareservice.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.adolescentcareservice.constants.Constants;
import com.ssf.adolescentcareservice.dtos.AuditDto;
import com.ssf.adolescentcareservice.dtos.CommoditiesProvided;
import com.ssf.adolescentcareservice.dtos.CouncelingData;
import com.ssf.adolescentcareservice.dtos.InformationCollected;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document(collection = Constants.ADOLESCENT_CARE_SERVICE_COLLECTION_NAME)
@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AdolescentCareService extends AuditDto {

	@Id
	private String id;
	private String childCitizenId;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = Constants.ISO_DATE_TIME_FORMAT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
	private LocalDateTime visitDate;
	private Boolean isReferredToPhc;
	private String servicedBy;
	private String servicedByName;
	private List<CommoditiesProvided> commoditiesProvided;
	private List<InformationCollected> informationCollected;
	private List<CouncelingData> councelingData;

}
