package phone.shared.exception;

import java.io.Serializable;

public class AtsCommunicationException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;

	public AtsCommunicationException() {
	}

	public AtsCommunicationException(String message) {
		super(message);
	}

}
