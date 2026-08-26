package phone.server.dto;

public class CallRequest {
	private String phoneNumber;

	public CallRequest() {
	}

	public CallRequest(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "CallRequest [phoneNumber=" + phoneNumber + "]";
	}

}
