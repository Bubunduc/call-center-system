package phone.server.sevice;

import java.util.List;

import phone.server.client.AtsClient;
import phone.server.dao.DeviceDao;
import phone.server.dao.RoomDao;
import phone.server.storage.PhoneStorage;
import phone.shared.model.Device;
import phone.shared.model.Room;

public class TelephonyService {

	private RoomDao roomdao;
	private DeviceDao devicedao;
	private AtsClient atsclient;
	private PhoneStorage phonestorage;

	public TelephonyService(RoomDao roomdao, DeviceDao devicedao, AtsClient atsclient, PhoneStorage phonestorage) {
		this.roomdao = roomdao;
		this.devicedao = devicedao;
		this.atsclient = atsclient;
		this.phonestorage = phonestorage;
	}
	
	public List<Room> getAllRooms(){
		return roomdao.findAll();
	}
	
	public List<Device> getAllDevices(){
		return devicedao.findAll();
	}

}
