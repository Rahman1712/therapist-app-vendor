package com.ar.therapist.vendor.exception;

public class TimeSlotNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TimeSlotNotFoundException(String message) {
		super(message);
	}
}
