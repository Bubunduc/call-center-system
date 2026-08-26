package phone.server;

import phone.server.client.AtsClient;
import phone.server.dao.DeviceDao;
import phone.server.dao.RoomDao;
import phone.server.dao.impl.DeviceDaoMyBatisImpl;
import phone.server.dao.impl.RoomDaoMyBatisImpl;
import phone.server.sevice.TelephonyService;
import phone.server.storage.PhoneStorage;

public final class ApplicationContext {

	private static final RoomDao roomDao = new RoomDaoMyBatisImpl();

	private static final DeviceDao deviceDao = new DeviceDaoMyBatisImpl();

	private static final AtsClient atsClient = new AtsClient();

	private static final PhoneStorage phoneStorage = new PhoneStorage();

	private static final TelephonyService telephonyService = new TelephonyService(roomDao, deviceDao, atsClient,
			phoneStorage);

	private ApplicationContext() {
	}

	public static TelephonyService getTelephonyService() {
		return telephonyService;
	}
}
