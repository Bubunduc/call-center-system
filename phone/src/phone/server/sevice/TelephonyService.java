package phone.server.sevice;

import java.util.ArrayList;
import java.util.List;

import phone.server.client.AtsClient;
import phone.server.dao.DeviceDao;
import phone.server.dao.RoomDao;
import phone.server.dto.AnswerCallRequest;
import phone.server.dto.CallRequest;
import phone.server.dto.CallResponse;
import phone.server.dto.EndCallRequest;
import phone.server.storage.PhoneStorage;
import phone.shared.dto.ActiveCall;
import phone.shared.dto.DeviceResponse;
import phone.shared.dto.PhoneResponse;
import phone.shared.dto.RoomResponse;
import phone.shared.exception.InvalidDeviceStateException;
import phone.shared.exception.TelephonyException;
import phone.shared.model.Device;

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

	public List<RoomResponse> getAllRooms() {
		return RoomResponse.toDto(roomdao.findAll());
	}

	public List<Device> getAllDevices() {
		return devicedao.findAll();
	}

	public void addToQueue(CallRequest call) throws TelephonyException {
		phoneStorage.addCallQueue(call);
	}

	public void removeFromQueue(CallRequest call) throws TelephonyException {
		phoneStorage.removeFromQueue(call);
	}

	public List<PhoneResponse> getNumsList() {
		return PhoneResponse.toDto(phoneStorage.getPhoneNumberList());
	}

	public void answerCall(AnswerCallRequest request) throws TelephonyException, InvalidDeviceStateException {
		Device device = getDeviceByNumber(request.getDeviceNumber());
		CallRequest numInQueue = new CallRequest(request.getPhoneNumber());
		phoneStorage.answerCall(device, request.getPhoneNumber());
		phoneStorage.removeFromQueue(numInQueue);
	}
	
	public void endCall(EndCallRequest request) throws TelephonyException, InvalidDeviceStateException {
		Device device = getDeviceByNumber(request.getDeviceNumber());
		phoneStorage.endCall(device.getDeviceNumber());
	}
	
	public List<ActiveCall> getActiveCallsList(){
		return phoneStorage.getActiveCallsList();
	}
	
	private Device getDeviceByNumber(String deviceNumber)throws TelephonyException {
		Device device = devicedao.findByDeviceNumber(deviceNumber);
		
		if (device == null) {
			throw new TelephonyException("Номер внутреннего аппарата не существует");
		}
		return device;
	}
	
	public List<DeviceResponse> getDevicesStatusByRoom(Long roomId){
		List<Device> devices = devicedao.findAllByRoomId(roomId);
		List<DeviceResponse> result = new ArrayList<DeviceResponse>();
		
		if (devices == null) {
			return new ArrayList<DeviceResponse>();
		}
		for(Device device : devices) {
			ActiveCall deviceInfo = phoneStorage.getActiveCallByDeviceNumber(device.getDeviceNumber());
			
			if(deviceInfo == null) {
				result.add(DeviceResponse.toDeviceResponse(device));
			}
			else {
				result.add(DeviceResponse.toDeviceResponse(deviceInfo));
			}
		}
		return result;
		
	} 
	
	public void sendToAts(CallResponse action) throws Exception {
		atsclient.sendAction(action);
	}
}
