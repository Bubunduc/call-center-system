package phone.client.store;

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
		deviceMap.put(device.getId(), device);
	}

	public void addActiveCall(ActiveCall call) {
		activeCallMap.put(call.getDeviceNumber(), call);
	}
	
	public boolean isDeviceBuisy(String id) {
		for (ActiveCall call :  activeCallMap.values()) {
			if(call.getDeviceNumber().equals(id)) {
				return true;
			}
		}
		return false;
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
	public void addToQueueList(List<PhoneResponse> phones){
		for(PhoneResponse phone : phones) {
			addToQueue(phone.getPhoneNumber());
		}
	}
	public String getNext() {
		return phonesQueue.peek();
	}
	}
