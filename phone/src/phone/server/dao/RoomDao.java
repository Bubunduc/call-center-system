package phone.server.dao;

import java.util.List;

import phone.shared.model.Room;

public interface RoomDao {
	List<Room> findAll();
	Room findRoomById(Long id);
}
