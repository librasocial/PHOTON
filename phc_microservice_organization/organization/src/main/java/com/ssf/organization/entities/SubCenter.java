package com.ssf.organization.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.organization.dtos.AuditDto;
import com.ssf.organization.model.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Node
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCenter extends AuditDto implements Org {
	@Id
	@GeneratedValue(generatorClass = UUIDStringGenerator.class)
	private String uuid;
	private String name;
	private Double latitude;
	private Double longitude;
	private String photo;
	private String facilityId;
	private String description;
	private String addressLine;
	private String stateName;
	private String districtName;
	private String talukName;
	private String villageName;
	private String pin;
	private String contact;
	private String email;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate registeredDate;
	private Boolean isRegistered;
	private StatusEnum status;
}
