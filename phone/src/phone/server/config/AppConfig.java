package phone.server.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

	private final Properties properties = new Properties();

	public AppConfig() {
		loadProperties();
		validate();
	}

	private void loadProperties() {
		try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {

			if (input == null) {
				throw new IllegalStateException("Файл application.properties не найден");
			}

			properties.load(input);

		} catch (IOException e) {
			throw new IllegalStateException("Не удалось загрузить application.properties", e);
		}
	}

	private void validate() {
		getRequired("ats.url");
		getRequired("internal.token");

		getRequired("db.url");
		getRequired("db.username");
		getRequired("db.password");

		getRequired("ats.allowed.origin");
		getRequired("swagger.allowed.origin");
	}

	public String getRequired(String key) {
		String value = properties.getProperty(key);

		if (value == null || value.trim().isEmpty()) {
			throw new IllegalStateException("Property " + key + " is not set");
		}

		return resolveEnvironmentVariable(value);
	}

	private String resolveEnvironmentVariable(String value) {

		if (value.startsWith("${") && value.endsWith("}")) {

			String envName = value.substring(2, value.length() - 1);
			String envValue = System.getenv(envName);

			if (envValue == null || envValue.trim().isEmpty()) {
				throw new IllegalStateException("Environment variable " + envName + " is not set");
			}

			return envValue;
		}

		return value;
	}
}
