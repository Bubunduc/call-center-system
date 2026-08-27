package phone.shared.exception;

import java.io.Serializable;

public class InvalidDeviceStateException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;

	public InvalidDeviceStateException() {
		
	}
	
	public InvalidDeviceStateException(String message) {
		super(message);
	}
}
