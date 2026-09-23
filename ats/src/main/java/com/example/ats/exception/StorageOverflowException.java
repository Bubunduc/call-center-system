package com.example.ats.exception;

public class StorageOverflowException extends Exception {

	private static final long serialVersionUID = 1L;

	public StorageOverflowException() {

	}

	public StorageOverflowException(String message) {
		super(message);
	}
}
