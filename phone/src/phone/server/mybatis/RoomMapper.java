package phone.server.mybatis;

import java.util.List;

import phone.shared.model.Room;

public interface RoomMapper {
	List<Room> findAll();
}
