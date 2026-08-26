package phone.server.dto;

import java.util.ArrayList;
import java.util.List;

public class PhoneResponse {
	private String phoneNumber;
	
	public PhoneResponse() {
		
	}
	
	
	
	public PhoneResponse(String phoneNumber) {
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
		return "phoneResponse [phoneNumber=" + phoneNumber + "]";
	}
	
	public static List<PhoneResponse> toDto(List<String> phones){
		List<PhoneResponse> responseList = new ArrayList<PhoneResponse>();
		for (String phone : phones) {
			responseList.add(new PhoneResponse(phone));
		}
		return responseList;
	}
	
}
