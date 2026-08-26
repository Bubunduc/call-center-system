package phone.server.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import phone.server.dao.DeviceDao;
import phone.server.mybatis.DeviceMapper;
import phone.server.mybatis.MyBatisUtil;
import phone.shared.model.Device;

public class DeviceDaoMyBatisImpl implements DeviceDao {

	@Override
	public List<Device> findAll() {
		try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
			DeviceMapper mapper = session.getMapper(DeviceMapper.class);
			return mapper.findAll();
		}
	}

}
