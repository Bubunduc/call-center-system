package phone.server;

import phone.server.client.AtsClient;
import phone.server.config.AppConfig;
import phone.server.dao.DeviceDao;
import phone.server.dao.RoomDao;
import phone.server.dao.impl.DeviceDaoMyBatisImpl;
import phone.server.dao.impl.RoomDaoMyBatisImpl;
import phone.server.mybatis.MyBatisUtil;
import phone.server.service.TelephonyService;
import phone.server.storage.PhoneStorage;

public final class ApplicationContext {

	private static final ApplicationContext INSTANCE = new ApplicationContext();

	private final AppConfig config;

	private final RoomDao roomDao;
	private final DeviceDao deviceDao;

	private final AtsClient atsClient;
	private final PhoneStorage phoneStorage;

	private final TelephonyService telephonyService;

	private ApplicationContext() {
		this.config = new AppConfig();
		MyBatisUtil.init(config);
		this.roomDao = new RoomDaoMyBatisImpl();
		this.deviceDao = new DeviceDaoMyBatisImpl();
		this.atsClient = new AtsClient(config);
		this.phoneStorage = new PhoneStorage();
		this.telephonyService = new TelephonyService(roomDao, deviceDao, atsClient, phoneStorage);
	}

	public static ApplicationContext getInstance() {
		return INSTANCE;
	}

	public TelephonyService getTelephonyService() {
		return telephonyService;
	}
}