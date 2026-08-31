package phone.client.store;

import java.util.HashMap;
import java.util.Map;

import phone.client.dto.DeviceInfo;
import phone.shared.dto.ActiveCall;

public class ClientPhoneStore {

	private final Map<String, DeviceInfo> deviceMap;
	private final Map<String, ActiveCall> activeCallMap;

	private String selectedTreeDevice;
	private String selectedActiveCallDevice;

	public ClientPhoneStore() {

		deviceMap = new HashMap<String, DeviceInfo>();
		activeCallMap = new HashMap<String, ActiveCall>();
	}

	public void addDevice(DeviceInfo device) {
		deviceMap.put(device.getId(), device);
	}

	public void addActiveCall(ActiveCall call) {
		activeCallMap.put(call.getDeviceNumber(), call);
	}

	public String getSelectedTreeDevice() {
		return selectedTreeDevice;
	}

	public void setSelectedTreeDevice(String selectedTreeDevice) {
		this.selectedTreeDevice = selectedTreeDevice;
	}

	public String getSelectedActiveCallDevice() {
		return selectedActiveCallDevice;
	}

	public void setSelectedActiveCallDevice(String selectedActiveCallDevice) {
		this.selectedActiveCallDevice = selectedActiveCallDevice;
	}

}
