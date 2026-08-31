package phone.client.dto;

public class DeviceInfo {
	private String id;
	private String operatorName;

	public DeviceInfo(String id, String operatorName) {
		super();
		this.id = id;
		this.operatorName = operatorName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	@Override
	public String toString() {
		return "DeviceInfo [id=" + id + ", operatorName=" + operatorName + "]";
	}

}
