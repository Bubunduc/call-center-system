package phone.server.dto;

public class EndCallRequest {
	private String deviceNumber;

	public EndCallRequest(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	@Override
	public String toString() {
		return "EndCallRequest [deviceNumber=" + deviceNumber + "]";
	}

}
