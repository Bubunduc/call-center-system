package phone.server.storage;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

import phone.server.dto.ActiveCall;
import phone.server.dto.CallRequest;
import phone.shared.exception.TelephonyException;

public class PhoneStorage {
	private final Deque<CallRequest> callsQueue;
	private final Set<ActiveCall> activeCalls;

	public PhoneStorage() {
		callsQueue = new ConcurrentLinkedDeque<CallRequest>();
		activeCalls = new HashSet<ActiveCall>();
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
			if(call.getPhoneNumber().equals(number)) {
				return call;
			}
		}
		return null;
	}
}
