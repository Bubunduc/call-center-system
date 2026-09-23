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

	public synchronized void addCallQueue(CallRequest call) throws TelephonyException {
		if (call == null) {
		    throw new TelephonyException("Данные звонка отсутствуют");
		}
		if (isExistsInQueue(call)) {
			throw new TelephonyException("Номер " + call.getPhoneNumber() + " уже существует");
		}
		if (findActiveCallByPhoneNumber(call) != null) {
			throw new TelephonyException("Номер " + call.getPhoneNumber() + " уже разговаривает");
		}
		callsQueue.addLast(call);
	}

	public ActiveCall findActiveCallByPhoneNumber(CallRequest call) {
		for (ActiveCall activeCall : activeCalls.values()) {
			if (activeCall.getPhoneNumber().equals(call.getPhoneNumber())) {
				return activeCall;
			}
		}
		return null;
	}

	private boolean isExistsInQueue(CallRequest call) {
		if ((call == null) || (call.getPhoneNumber() == null) || (call.getPhoneNumber().isEmpty())) {
			return false;
		}
		List<String> numsList = getPhoneNumberList();

		return numsList.contains(call.getPhoneNumber());
	}

	public synchronized void removeFromQueue(CallRequest call) throws TelephonyException {
		if (call == null) {
		    throw new TelephonyException("Данные звонка отсутствуют");
		}
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
		if (callsQueue.isEmpty()) {
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

	public synchronized void addActiveCall(Device device, String number)
			throws TelephonyException, InvalidDeviceStateException {
		CallRequest call = getCallRequestByNumber(number);

		if (call == null) {
			throw new TelephonyException("Номер входящего не найден в очереди");
		}
		if (isDeviceActive(device.getDeviceNumber())) {
			throw new InvalidDeviceStateException("Внутренний аппарат занят");
		}
		ActiveCall newCall = new ActiveCall(
				device.getDeviceNumber(),
				device.getOperatorName(),
				number);
		removeFromQueue(call);
		activeCalls.put(device.getDeviceNumber(), newCall);
	}

	public List<ActiveCall> getActiveCallsList() {
		return new ArrayList<ActiveCall>(activeCalls.values());
	}

	public synchronized void removeActiveCall(String deviceNumber) throws InvalidDeviceStateException {
		ActiveCall activeCall = activeCalls.remove(deviceNumber);

		if (activeCall == null) {
			throw new InvalidDeviceStateException("Аппарат свободен и ни с кем не разговаривает");
		}

	}

	private boolean isDeviceActive(String deviceNumber) {
		if (deviceNumber == null) {
			return false;
		}
		return activeCalls.containsKey(deviceNumber);
	}

	public ActiveCall getActiveCallByDeviceNumber(String deviceNumber) {
		if (isDeviceActive(deviceNumber)) {
			return activeCalls.get(deviceNumber);
		}
		return null;
	}

	public synchronized void restoreActiveCall(ActiveCall activeCall) throws InvalidDeviceStateException {
		if (isDeviceActive(activeCall.getDeviceNumber())) {
			throw new InvalidDeviceStateException("Внутренний аппарат занят");
		}
		activeCalls.put(activeCall.getDeviceNumber(), activeCall);
	}

	public synchronized void restoreCallToQueue(CallRequest call) throws TelephonyException {
		if (call == null) {
		    throw new TelephonyException("Данные звонка отсутствуют");
		}
		if (isExistsInQueue(call)) {
			throw new TelephonyException("Номер " + call.getPhoneNumber() + " уже существует");
		}
		callsQueue.addFirst(call);
	}
	
	public synchronized ActiveCall removeCall(CallRequest call) throws TelephonyException, InvalidDeviceStateException {
	    ActiveCall currentCall = findActiveCallByPhoneNumber(call);
	    if (currentCall != null) {
	        removeActiveCall(currentCall.getDeviceNumber());
	        return currentCall;
	    }
	    removeFromQueue(call);
	    return null;
	}
	
	public synchronized ActiveCall removeActiveCallByDeviceNumber(String deviceNumber)throws InvalidDeviceStateException {
	    ActiveCall activeCall = activeCalls.get(deviceNumber);
	    if (activeCall == null) {
	        throw new InvalidDeviceStateException("Аппарат свободен и ни с кем не разговаривает");
	    }
	    activeCalls.remove(deviceNumber);
	    return activeCall;
	}

}
