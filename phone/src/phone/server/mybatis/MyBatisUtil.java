package phone.server.mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public final class MyBatisUtil {

	private static final SqlSessionFactory SQL_SESSION_FACTORY;

	static {
		try {
			String resource = "mybatis-config.xml";

			InputStream inputStream = Resources.getResourceAsStream(resource);

			Properties properties = new Properties();

			properties.setProperty("db.url", getRequiredEnv("DB_URL"));

			properties.setProperty("db.username", getRequiredEnv("DB_USERNAME"));

			properties.setProperty("db.password", getRequiredEnv("DB_PASSWORD"));

			SQL_SESSION_FACTORY = new SqlSessionFactoryBuilder().build(inputStream, properties);

		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private MyBatisUtil() {
	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return SQL_SESSION_FACTORY;
	}

	private static String getRequiredEnv(String name) {
		String value = System.getenv(name);

		if (value == null || value.trim().isEmpty()) {
			throw new IllegalStateException("Environment variable " + name + " is not set");
		}

		return value;
	}
}