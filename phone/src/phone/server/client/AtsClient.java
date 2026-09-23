package phone.server.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import phone.server.config.AppConfig;
import phone.server.dto.CallResponse;
import phone.server.dto.ErrorMessage;
import phone.shared.exception.AtsCommunicationException;

public class AtsClient {

	private final String atsUrl;
	private final String internalToken;
	private final Gson gson;

	public AtsClient(AppConfig config) {
		this.atsUrl = config.getRequired("ats.url");
		this.internalToken = config.getRequired("internal.token");

		this.gson = new GsonBuilder().setDateFormat("dd.MM.yyyy HH:mm:ss.SSS").create();
	}

	public void sendAction(CallResponse response) throws AtsCommunicationException {
		HttpURLConnection conn = null;

		try {
			URL url = new URL(atsUrl);

			conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("X-Internal-Token", internalToken);

			conn.setDoOutput(true);
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);

			String jsonResponse = gson.toJson(response);

			byte[] inputBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);

			try (OutputStream os = conn.getOutputStream()) {
				os.write(inputBytes);
			}

			int responseCode = conn.getResponseCode();

			if (responseCode < 200 || responseCode >= 300) {
				String errorMessage = getErrorMessage(conn);

				throw new AtsCommunicationException(
						"АТС вернула ошибку: " + errorMessage + ". Код ответа: " + responseCode);
			}

		} catch (AtsCommunicationException e) {
			throw e;

		} catch (Exception e) {
			throw new AtsCommunicationException("АТС сервер недоступен. Повторите попытку позже", e);

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	private String getErrorMessage(HttpURLConnection conn) throws IOException, AtsCommunicationException {
		InputStream errorStream = conn.getErrorStream();

		if (errorStream == null) {
			return conn.getResponseMessage();
		}

		try (InputStreamReader reader = new InputStreamReader(errorStream, StandardCharsets.UTF_8)) {

			ErrorMessage error = gson.fromJson(reader, ErrorMessage.class);

			if (error != null && error.getMessage() != null) {
				return error.getMessage();
			}

		} catch (Exception e) {
			throw new AtsCommunicationException("Неожиданное тело ответа");
		}

		return conn.getResponseMessage();
	}
}
