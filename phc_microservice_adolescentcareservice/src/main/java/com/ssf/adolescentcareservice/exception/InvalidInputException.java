package com.ssf.adolescentcareservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends Exception {

	private static final long serialVersionUID = 1L;

	private final HttpStatus status = HttpStatus.BAD_REQUEST;

	public InvalidInputException(String message) {
		super(message);
	}

	public HttpStatus getStatus() {
		return this.status;
	}
}
