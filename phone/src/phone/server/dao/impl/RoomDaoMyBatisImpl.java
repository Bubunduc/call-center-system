package phone.server.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import phone.server.dao.RoomDao;
import phone.server.mybatis.MyBatisUtil;
import phone.server.mybatis.RoomMapper;
import phone.shared.model.Room;

public class RoomDaoMyBatisImpl implements RoomDao {

	@Override
	public List<Room> findAll() {
		try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
			RoomMapper mapper = session.getMapper(RoomMapper.class);
			return mapper.findAll();
		}
	}

}
