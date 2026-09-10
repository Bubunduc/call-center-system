package com.example.ats.exception;

import java.io.Serializable;

public class EventValidationException extends Exception implements Serializable{

	private static final long serialVersionUID = 1L;

	public EventValidationException() {
		
	}
	
	public EventValidationException(String message) {
		super(message);
	}
}
