package phone.server.client;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import phone.server.dto.CallResponse;
import phone.shared.exception.AtsCommunicationException;

public class AtsClient {

	private final String atsUrl;

	public AtsClient() {
		atsUrl = System.getenv("ATS_URL");

		if (atsUrl == null || atsUrl.isEmpty()) {
			throw new IllegalStateException("Environment variable ATS_URL is not set");
		}
	}

	public void sendAction(CallResponse response) throws AtsCommunicationException {

		HttpURLConnection conn = null;

		try {
			URL url = new URL(atsUrl);

			conn = (HttpURLConnection) url.openConnection();

			Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy HH:mm:ss.SSS").create();

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

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
				throw new AtsCommunicationException("АТС вернула HTTP " + responseCode);
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
}