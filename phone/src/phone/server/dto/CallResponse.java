package phone.server.dto;

import java.sql.Timestamp;

import phone.server.enums.Status;

public class CallResponse {
	String phoneNumber;
	Timestamp timeStamp;
	String deviceName;
	String operatorName;
	Status status;
}
