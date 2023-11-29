package com.ssf.childcareimmunization.dtos;

import java.time.LocalDateTime;

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
public class DeathDto {
	private String place;
	private String cause;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = Constants.ISO_DATE_TIME_FORMAT)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
	private LocalDateTime date;
}
