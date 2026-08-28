package phone.server.storage;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

import phone.server.dto.CallRequest;
import phone.shared.dto.ActiveCall;
import phone.shared.exception.InvalidDeviceStateException;
import phone.shared.exception.TelephonyException;
import phone.shared.model.Device;

public class PhoneStorage {
	private final Deque<CallRequest> callsQueue;
	private final Map<String, ActiveCall> activeCalls;

	public PhoneStorage() {
		callsQueue = new ConcurrentLinkedDeque<CallRequest>();
		activeCalls = new ConcurrentHashMap<String, ActiveCall>();
	}

	public void addCallQueue(CallRequest call) throws TelephonyException {
		if (isExistsInQueue(call)) {
			throw new TelephonyException("Номер " + call.getPhoneNumber() + " уже существует");
		}
		callsQueue.addFirst(call);
	}

	public boolean isExistsInQueue(CallRequest call) {
		if ((call == null) || (call.getPhoneNumber() == null) || (call.getPhoneNumber().isEmpty())) {
			return false;
		}
		List<String> numsList = getPhoneNumberList();

		return numsList.contains(call.getPhoneNumber());
	}

	public void removeFromQueue(CallRequest call) throws TelephonyException {
		if (!isExistsInQueue(call)) {
			throw new TelephonyException("Номер " + call.getPhoneNumber() + " не существует");
		}
		CallRequest currentCall = getCallRequestByNumber(call.getPhoneNumber());
		if (currentCall == null) {
			throw new TelephonyException("Номер " + call.getPhoneNumber() + " не существует");
		}
		callsQueue.remove(currentCall);
	}

	public List<String> getPhoneNumberList() {
		if ((callsQueue == null) || (callsQueue.isEmpty())) {
			return new ArrayList<String>();
		}
		return callsQueue.stream().map(x -> x.getPhoneNumber()).collect(Collectors.toList());
	}

	private CallRequest getCallRequestByNumber(String number) {
		for (CallRequest call : callsQueue) {
			if (call.getPhoneNumber().equals(number)) {
				return call;
			}
		}
		return null;
	}

	public void answerCall(Device device, String number) throws TelephonyException, InvalidDeviceStateException {
		if (getCallRequestByNumber(number) == null) {
			throw new TelephonyException("Номер входящего не найден в очереди");
		}
		if (isDeviceActive(device.getDeviceNumber())) {
			throw new InvalidDeviceStateException("Внутренний аппарат занят");
		}
		ActiveCall newCall = new ActiveCall(device.getDeviceNumber(), device.getOperatorName(), number);
		activeCalls.put(device.getDeviceNumber(), newCall);
	}

	public List<ActiveCall> getActiveCallsList() {
		return new ArrayList<ActiveCall>(activeCalls.values());
	}

	public void endCall(String deviceNumber) throws InvalidDeviceStateException {
		if (!isDeviceActive(deviceNumber)) {
			throw new InvalidDeviceStateException("аппарат свободен и ни с кем не разговаривает");
		}
		activeCalls.remove(deviceNumber);
	}
	
	private boolean isDeviceActive (String deviceNumber) {
		return activeCalls.containsKey(deviceNumber);
	}
	
	public ActiveCall getActiveCallByDeviceNumber(String deviceNumber) {
		if(isDeviceActive(deviceNumber)) {
			return activeCalls.get(deviceNumber);
		}
		return null;
	}
	
}
