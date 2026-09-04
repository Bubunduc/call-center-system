package phone.server.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import phone.server.client.AtsClient;
import phone.server.dao.DeviceDao;
import phone.server.dao.RoomDao;
import phone.server.dto.AnswerCallRequest;
import phone.server.dto.CallRequest;
import phone.server.dto.CallResponse;
import phone.server.dto.EndCallRequest;
import phone.server.enums.Status;
import phone.server.storage.PhoneStorage;
import phone.shared.FieldVerifier;
import phone.shared.dto.ActiveCall;
import phone.shared.dto.DeviceResponse;
import phone.shared.dto.PhoneResponse;
import phone.shared.dto.RoomResponse;
import phone.shared.exception.InvalidDeviceStateException;
import phone.shared.exception.InvalidPhoneFormatException;
import phone.shared.exception.TelephonyException;
import phone.shared.model.Device;
import phone.shared.model.Room;

public class TelephonyService {

	private final RoomDao roomdao;
	private final DeviceDao devicedao;
	private final AtsClient atsclient;
	private final PhoneStorage phoneStorage;

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

	public void addToQueue(CallRequest call) throws TelephonyException, Exception, InvalidPhoneFormatException {

		validateCallRequest(call);
		phoneStorage.addCallQueue(call);
		CallResponse toAtsData = new CallResponse(
				call.getPhoneNumber(),
				null, 
				null,
				new Timestamp(System.currentTimeMillis()),
				Status.INCOMING);
		sendToAts(toAtsData);
	}

	public void removeFromQueue(CallRequest call) throws TelephonyException, Exception {
		phoneStorage.removeFromQueue(call);
		CallResponse toAtsData = new CallResponse(
				call.getPhoneNumber(), 
				null,
				null,
				new Timestamp(System.currentTimeMillis()), 
				Status.CANCELED);
		sendToAts(toAtsData);
	}

	public List<PhoneResponse> getNumsList() {
		return PhoneResponse.toDto(phoneStorage.getPhoneNumberList());
	}

	public void answerCall(AnswerCallRequest request)
			throws TelephonyException, InvalidDeviceStateException, Exception {
		Device device = getDeviceByNumber(request.getDeviceNumber());
		CallRequest numInQueue = new CallRequest(request.getPhoneNumber());
		phoneStorage.answerCall(device, request.getPhoneNumber());
		phoneStorage.removeFromQueue(numInQueue);

		CallResponse toAtsData = new CallResponse(
				request.getPhoneNumber(), 
				device.getDeviceNumber(),
				device.getOperatorName(),
				new Timestamp(System.currentTimeMillis()),
				Status.ANSWERED);

		sendToAts(toAtsData);
	}

	public void endCall(EndCallRequest request) throws TelephonyException, InvalidDeviceStateException, Exception {
		Device device = getDeviceByNumber(request.getDeviceNumber());

		ActiveCall activeCall = phoneStorage.endCall(device.getDeviceNumber());

		CallResponse toAtsData = new CallResponse(
				activeCall.getPhoneNumber(),
				device.getDeviceNumber(),
				device.getOperatorName(),
				new Timestamp(System.currentTimeMillis()),
				Status.HANG_UP);

		sendToAts(toAtsData);
	}

	public List<ActiveCall> getActiveCallsList() {
		return phoneStorage.getActiveCallsList();
	}

	private Device getDeviceByNumber(String deviceNumber) throws TelephonyException {
		Device device = devicedao.findByDeviceNumber(deviceNumber);

		if (device == null) {
			throw new TelephonyException("Номер внутреннего аппарата не существует");
		}
		return device;
	}

	public List<DeviceResponse> getDevicesStatusByRoom(Long roomId) throws TelephonyException {
		Room room = roomdao.findRoomById(roomId);
		if (room == null) {
			throw new TelephonyException("Комнаты с таким id не существует");
		}
		List<Device> devices = devicedao.findAllByRoomId(roomId);
		List<DeviceResponse> result = new ArrayList<DeviceResponse>();

		if (devices == null) {
			return new ArrayList<DeviceResponse>();
		}
		for (Device device : devices) {
			ActiveCall deviceInfo = phoneStorage.getActiveCallByDeviceNumber(device.getDeviceNumber());

			if (deviceInfo == null) {
				result.add(DeviceResponse.toDeviceResponse(device));
			} else {
				result.add(DeviceResponse.toDeviceResponse(deviceInfo));
			}
		}
		return result;

	}

	private void sendToAts(CallResponse action) throws Exception {
		atsclient.sendAction(action);
	}

	private void validateCallRequest(CallRequest callRequest) throws InvalidPhoneFormatException {
		String error = FieldVerifier.verifyIncomingPhone(callRequest);
		if (error != null) {
			throw new InvalidPhoneFormatException(error);
		}
	}
}
