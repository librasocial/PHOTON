package com.ssf.organization.exceptions;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
@AllArgsConstructor
public class OrgError {
	 
	private HttpStatus status;
	private String message;
	//private List<String> errors;
}
