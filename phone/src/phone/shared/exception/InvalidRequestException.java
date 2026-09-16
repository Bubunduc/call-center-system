package phone.shared.exception;

import java.io.Serializable;

public class InvalidRequestException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;

	public InvalidRequestException() {

	}

	public InvalidRequestException(String message) {
		super(message);
	}
}
