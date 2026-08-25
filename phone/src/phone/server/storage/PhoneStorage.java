package phone.server.storage;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import phone.server.dto.ActiveCall;
import phone.server.dto.CallRequest;

public class PhoneStorage {
	private static final Deque<CallRequest> callsQueue;
	private static final Set<ActiveCall> activeCalls;
	
	
	static {
		callsQueue = new LinkedList<CallRequest>();
		activeCalls = new HashSet<ActiveCall>();
	}
	
	public static void addCallQueue(CallRequest call) {
		callsQueue.addFirst(call);
	}
	public static void removeCall() {
		
	}
}
