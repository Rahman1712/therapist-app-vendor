package com.ar.therapist.vendor.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	private String errorMessage;
	private String fieldName;
	private String fieldvalue;
}