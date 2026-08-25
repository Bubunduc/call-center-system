package phone.server.dto;

public class ActiveCall {
	private String deviceName;
	private String operatorName;
	private String phoneNumber;

	public ActiveCall(String deviceName, String operatorName, String phoneNumber) {
		super();
		this.deviceName = deviceName;
		this.operatorName = operatorName;
		this.phoneNumber = phoneNumber;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "ActiveCall [deviceName=" + deviceName + ", operatorName=" + operatorName + ", phoneNumber="
				+ phoneNumber + "]";
	}

}
