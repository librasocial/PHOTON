package com.ssf.tbsurveillance.dtos;

import java.time.LocalDateTime;

import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssf.tbsurveillance.constants.Constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SurveillanceStatusDto {
	@JsonProperty("citizenId")
	private String _id;
	private String result;
	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
	private LocalDateTime resultDate;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public LocalDateTime getResultDate() {
		return resultDate;
	}

	public void setResultDate(LocalDateTime resultDate) {
		this.resultDate = resultDate;
	}

}
