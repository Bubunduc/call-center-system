package phone.server.dao;

import java.util.List;

import phone.shared.model.Device;

public interface DeviceDao {
	List<Device> findAll();
}
