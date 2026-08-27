package phone.server.dto;

public class AnswerCallRequest {

	private String deviceNumber;
	private String phoneNumber;

	public AnswerCallRequest() {
	}

	public AnswerCallRequest(String deviceNumber, String phoneNumber) {
		this.deviceNumber = deviceNumber;
		this.phoneNumber = phoneNumber;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
