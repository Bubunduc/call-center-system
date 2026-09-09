package phone.client.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import phone.client.dto.DeviceInfo;
import phone.shared.dto.ActiveCall;
import phone.shared.dto.PhoneResponse;

public class ClientPhoneStore {

	private final Map<String, DeviceInfo> deviceMap;
	private final Map<String, ActiveCall> activeCallMap;
	private final Queue<String> phonesQueue;
	private String selectedTreeDeviceId;
	private String selectedActiveCallId;

	public ClientPhoneStore() {

		deviceMap = new HashMap<String, DeviceInfo>();
		activeCallMap = new HashMap<String, ActiveCall>();
		phonesQueue = new LinkedList<String>();
	}

	public void addDevice(DeviceInfo device) {
		if (device == null || device.getId() == null) {
			return;
		}
		deviceMap.put(device.getId(), device);
	}

	public void addActiveCall(ActiveCall call) {
		if (call == null || call.getDeviceNumber() == null || call.getDeviceNumber().isEmpty()) {
			return;
		}
		activeCallMap.put(call.getDeviceNumber(), call);
	}

	public void removeActiveCall(String deviceNumber) {
		activeCallMap.remove(deviceNumber);
	}

	public boolean isDeviceBusy(String id) {
		return activeCallMap.containsKey(id);
	}

	public String getSelectedTreeDeviceId() {
		return selectedTreeDeviceId;
	}

	public void setSelectedTreeDeviceId(String selectedTreeDevice) {
		this.selectedTreeDeviceId = selectedTreeDevice;
	}

	public String getSelectedActiveCallId() {
		return selectedActiveCallId;
	}

	public void setSelectedActiveCallId(String selectedActiveCallDevice) {
		this.selectedActiveCallId = selectedActiveCallDevice;
	}

	public DeviceInfo getSelectedDevice() {
		return deviceMap.get(selectedTreeDeviceId);
	}

	public void addToQueue(String phone) {
		if (phone == null || phone.isEmpty()) {
			return;
		}
		if (phonesQueue.contains(phone)) {
			return;
		}
		phonesQueue.add(phone);
	}

	public void pushQueue() {
		if (phonesQueue.isEmpty()) {
			return;
		}
		phonesQueue.remove();

	}

	public void addToQueueList(List<PhoneResponse> phones) {
		for (PhoneResponse phone : phones) {
			if (phone != null) {
				addToQueue(phone.getPhoneNumber());
			}
		}
	}

	public String getNext() {
		return phonesQueue.peek();
	}

	public boolean updateQueue(List<PhoneResponse> response) {

		if (response == null) {
			return false;
		}
		List<String> phones = new ArrayList<String>();
		for (PhoneResponse phone : response) {

			if (phone != null && phone.getPhoneNumber() != null) {
				phones.add(phone.getPhoneNumber());
			}

		}
		List<String> currentList = new ArrayList<String>(phonesQueue);
		if (phones.equals(currentList)) {
			return false;
		}
		phonesQueue.clear();
		phonesQueue.addAll(phones);
		return true;
	}

	public boolean updateActiveCalls(List<ActiveCall> calls) {
		
		ActiveCall selectedCall = activeCallMap.get(selectedActiveCallId);
		Map<String, ActiveCall> newMap = toActiveCallsMap(calls);
		if (!activeCallMap.equals(newMap)) {
			activeCallMap.clear();
			activeCallMap.putAll(newMap);
			if (selectedCall != null && !activeCallMap.containsKey(selectedActiveCallId)
					&& !activeCallMap.containsValue(selectedCall)) {
				setSelectedActiveCallId(null);
			}
			return true;
		}
		return false;

	}

	private Map<String, ActiveCall> toActiveCallsMap(List<ActiveCall> calls) {

		Map<String, ActiveCall> result = new HashMap<String, ActiveCall>();
		if (calls == null) {
			return result;
		}
		for (ActiveCall call : calls) {
			if (call == null || call.getDeviceNumber() == null || call.getDeviceNumber().isEmpty()) {
				continue;
			}
			result.put(call.getDeviceNumber(), call);
		}
		return result;
	}

}
