package com.ssf.bssurveillance.dtos;

import java.time.LocalDateTime;

import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssf.bssurveillance.constant.BSSurveillanceConst;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SurveillanceStatusDto {
	@JsonProperty("citizenId")
	private String _id;
	private String result;
	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BSSurveillanceConst.ISO_DATE_TIME_FORMAT)
	private LocalDateTime dateModified;
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
	public LocalDateTime getDateModified() {
		return dateModified;
	}
	public void setDateModified(LocalDateTime dateModified) {
		this.dateModified = dateModified;
	}
	
	
}
