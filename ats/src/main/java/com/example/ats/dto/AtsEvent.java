package com.example.ats.dto;

import java.sql.Timestamp;

import com.example.ats.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

public class AtsEvent {
	private String phoneNumber;

	private String deviceNumber;

	private String operatorName;

	@JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss.SSS")
	private Timestamp timeStamp;
	private Status status;

	public AtsEvent() {
	}

	public AtsEvent(String phoneNumber, String deviceNumber, String operatorName, Timestamp timeStamp, Status status) {
		this.phoneNumber = phoneNumber;
		this.timeStamp = timeStamp;
		this.deviceNumber = deviceNumber;
		this.operatorName = operatorName;
		this.status = status;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CallResponse [phoneNumber=" + phoneNumber + ", timeStamp=" + timeStamp + ", deviceNumber="
				+ deviceNumber + ", operatorName=" + operatorName + ", status=" + status + "]";
	}

}
