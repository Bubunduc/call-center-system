package phone.server.mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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

			SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream, properties);

			try (Connection connection = factory.getConfiguration().getEnvironment().getDataSource().getConnection();
					Statement statement = connection.createStatement()) {

				statement.setQueryTimeout(5);
				statement.execute("SELECT 1"); //проверка бд при запуске
			}

			sqlSessionFactory = factory;

		} catch (IOException | SQLException e) {
			throw new IllegalStateException("Не удалось инициализировать MyBatis или подключиться к БД", e);
		}
	}

	public static SqlSessionFactory getSqlSessionFactory() {
		if (sqlSessionFactory == null) {
			throw new IllegalStateException("MyBatisUtil не инициализирован");
		}

		return sqlSessionFactory;
	}
}