package phone.shared.dto;

import java.util.Objects;

public class ActiveCall {
	private String deviceNumber;
	private String operatorName;
	private String phoneNumber;

	public ActiveCall(String deviceNumber, String operatorName, String phoneNumber) {
		this.deviceNumber = deviceNumber;
		this.operatorName = operatorName;
		this.phoneNumber = phoneNumber;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
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
		return "ActiveCall [deviceNumber=" + deviceNumber + ", operatorName=" + operatorName + ", phoneNumber="
				+ phoneNumber + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(deviceNumber, operatorName, phoneNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActiveCall other = (ActiveCall) obj;
		return Objects.equals(deviceNumber, other.deviceNumber) && Objects.equals(operatorName, other.operatorName)
				&& Objects.equals(phoneNumber, other.phoneNumber);
	}

}
