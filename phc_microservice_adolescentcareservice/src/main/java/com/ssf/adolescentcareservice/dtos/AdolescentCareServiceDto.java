package com.ssf.adolescentcareservice.dtos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssf.adolescentcareservice.constants.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdolescentCareServiceDto {
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
	private AuditDto audit;

}
