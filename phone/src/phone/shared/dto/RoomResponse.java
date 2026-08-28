package phone.shared.dto;

import java.util.ArrayList;
import java.util.List;

import phone.shared.model.Room;

public class RoomResponse {
	private Long id;
	private String name;

	public RoomResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Room [id=" + id + ", name=" + name + "]";
	}
	
	public static List<RoomResponse> toDto(List<Room> rooms){
		
		List<RoomResponse> result = new ArrayList<RoomResponse>();
		
		for (Room room : rooms) {
			result.add(new RoomResponse(room.getId(), room.getName()));
		}
		
		return result;
	}
}
