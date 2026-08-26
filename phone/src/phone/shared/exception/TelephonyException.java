package phone.shared.exception;

import java.io.Serializable;

public class TelephonyException extends Exception implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public TelephonyException() {
		
	}
	
	public TelephonyException(String message) {
		super(message);
	}

}
