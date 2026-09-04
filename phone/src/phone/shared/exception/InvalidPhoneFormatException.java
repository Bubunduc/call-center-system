package phone.shared.exception;

import java.io.Serializable;

public class InvalidPhoneFormatException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;

	public InvalidPhoneFormatException() {

	}

	public InvalidPhoneFormatException(String message) {
		super(message);
	}
}
