package phone.server.dto;

import phone.shared.model.Device;

public class DeviceResponse {
	private String deviceNumber;
	private String operatorName;
	private String incomingNumber;
	
	public DeviceResponse() {
	}
	
	public DeviceResponse(String deviceNumber, String operatorName, String incomingNumber) {
		this.deviceNumber = deviceNumber;
		this.operatorName = operatorName;
		this.incomingNumber = incomingNumber;
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

	public String getIncomingNumber() {
		return incomingNumber;
	}

	public void setIncomingNumber(String incomingNumber) {
		this.incomingNumber = incomingNumber;
	}

	@Override
	public String toString() {
		return "DeviceResponse [deviceNumber=" + deviceNumber + ", operatorName=" + operatorName + ", incomingNumber="
				+ incomingNumber + "]";
	}
	
	public static DeviceResponse toDeviceResponse(Device deviceinfo) {
		return new DeviceResponse(deviceinfo.getDeviceNumber(),deviceinfo.getOperatorName(),null);
	}
	public static DeviceResponse toDeviceResponse(ActiveCall deviceinfo) {
		return new DeviceResponse(deviceinfo.getDeviceNumber(),deviceinfo.getOperatorName(),deviceinfo.getPhoneNumber());
	}
	
	
	
}
