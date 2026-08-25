package phone.server.dto;

import java.sql.Timestamp;

public class CallRequest {
	private String phoneNumber;
	private Timestamp timestamp;

	public CallRequest(String phoneNumber, Timestamp timestamp) {
		this.phoneNumber = phoneNumber;
		this.timestamp = timestamp;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "CallRequest [phoneNumber=" + phoneNumber + ", timestamp=" + timestamp + "]";
	}

}
