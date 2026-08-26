package phone.shared.model;

import java.io.Serializable;

public class Device implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private Long roomId;

	private String deviceNumber;

	private String operatorName;

	public Device(Long id, Long roomId, String deviceNumber, String operatorName) {
		this.id = id;
		this.roomId = roomId;
		this.deviceNumber = deviceNumber;
		this.operatorName = operatorName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
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

	@Override
	public String toString() {
		return "Device [id=" + id + ", roomId=" + roomId + ", deviceNumber=" + deviceNumber + ", operatorName="
				+ operatorName + "]";
	}

}
