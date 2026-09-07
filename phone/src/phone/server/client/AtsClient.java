package phone.server.client;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import phone.server.dto.CallResponse;

public class AtsClient {

	private final String atsUrl;

	public AtsClient() {
		atsUrl = System.getenv("ATS_URL");

		if (atsUrl == null || atsUrl.isEmpty()) {
			throw new IllegalStateException("Environment variable ATS_URL is not set");
		}
	}

	public void sendAction(CallResponse response) throws Exception {

		URL url = new URL(atsUrl);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy HH:mm:ss.SSS").create();

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

		conn.setDoOutput(true);
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000);

		String jsonResponse = gson.toJson(response);

		byte[] inputBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);

		try {
			try (OutputStream os = conn.getOutputStream()) {
				os.write(inputBytes);
			}

			int responseCode = conn.getResponseCode();

			if (responseCode < 200 || responseCode >= 300) {
				throw new RuntimeException("АТС вернула HTTP " + responseCode);
			}

		} finally {
			conn.disconnect();
		}
	}
}