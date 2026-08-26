package phone.server.mybatis;

import java.util.List;

import phone.shared.model.Device;

public interface DeviceMapper {
	List<Device> findAll();
}
