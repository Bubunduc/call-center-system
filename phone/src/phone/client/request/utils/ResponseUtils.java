package phone.client.request.utils;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;

public final class ResponseUtils {

	private ResponseUtils() {
	}

	public static String getErrorMessage(Response response) {
		if (response.getStatusCode() == 0) {
			return "Сервер недоступен";
		}
		try {
			JSONObject object = JsonUtils.parseObject(response.getText());

			String message = JsonUtils.getString(object, "message");

			if (message != null) {
				return message;
			}

		} catch (Exception e) {
			return "Ответ сервера не является корректным JSON";
		}

		return "Ошибка сервера, код ответа: " + response.getStatusCode();
	}
}