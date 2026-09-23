package phone.server.mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import phone.server.config.AppConfig;

public final class MyBatisUtil {

	private static SqlSessionFactory sqlSessionFactory;

	private MyBatisUtil() {
	}

	public static void init(AppConfig config) {
		if (sqlSessionFactory != null) {
			return;
		}

		String resource = "mybatis-config.xml";

		try (InputStream inputStream = Resources.getResourceAsStream(resource)) {

			Properties properties = new Properties();

			properties.setProperty("db.url", config.getRequired("db.url"));

			properties.setProperty("db.username", config.getRequired("db.username"));

			properties.setProperty("db.password", config.getRequired("db.password"));

			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, properties);

		} catch (IOException e) {
			throw new IllegalStateException("Не удалось инициализировать MyBatis", e);
		}
	}

	public static SqlSessionFactory getSqlSessionFactory() {
		if (sqlSessionFactory == null) {
			throw new IllegalStateException("MyBatisUtil не инициализирован");
		}

		return sqlSessionFactory;
	}
}