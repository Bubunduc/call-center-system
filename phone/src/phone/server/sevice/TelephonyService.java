package phone.server.sevice;

import java.util.List;

import phone.server.client.AtsClient;
import phone.server.dao.DeviceDao;
import phone.server.dao.RoomDao;
import phone.server.dto.CallRequest;
import phone.server.dto.PhoneResponse;
import phone.server.storage.PhoneStorage;
import phone.shared.exception.TelephonyException;
import phone.shared.model.Device;
import phone.shared.model.Room;

public class TelephonyService {

	private RoomDao roomdao;
	private DeviceDao devicedao;
	private AtsClient atsclient;
	private PhoneStorage phoneStorage;

	public TelephonyService(RoomDao roomdao, DeviceDao devicedao, AtsClient atsclient, PhoneStorage phonestorage) {
		this.roomdao = roomdao;
		this.devicedao = devicedao;
		this.atsclient = atsclient;
		this.phoneStorage = phonestorage;
	}
	
	public List<Room> getAllRooms(){
		return roomdao.findAll();
	}
	
	public List<Device> getAllDevices(){
		return devicedao.findAll();
	}
	
	public void addToQueue(CallRequest call) throws TelephonyException {
		phoneStorage.addCallQueue(call);
	}
	
	public void removeFromQueue(CallRequest call) throws TelephonyException {
		phoneStorage.removeFromQueue(call);;
	}
	
	public List<PhoneResponse> getNumsList(){
		return PhoneResponse.toDto(phoneStorage.getPhoneNumberList());
	}

	
	

}
