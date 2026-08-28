package phone.server.client;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

import phone.server.dto.CallResponse;

public class AtsClient {

	private final String URL = "http://127.0.0.1:8080/api/action";

	public void sendAction(CallResponse response) throws Exception {

		URL url = new URL(URL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		Gson gson = new Gson();

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; utf-8");
		conn.setDoOutput(true);
		conn.setConnectTimeout(5000);

		String jsonResponse = gson.toJson(response);
		byte[] inputBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);

		try (OutputStream os = conn.getOutputStream()) {
			os.write(inputBytes, 0, inputBytes.length);
		}

		try {

			int responseCode = conn.getResponseCode();

			if (responseCode < 200 || responseCode >= 300) {

				System.err.println("Сервер вернул ошибку: " + responseCode);
			}
		} finally {

			conn.disconnect();
		}
	}

}
