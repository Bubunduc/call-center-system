package phone.client.request.utils;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public final class JsonUtils {

	private JsonUtils() {
	}

	public static String getString(JSONObject object, String fieldName) {
		if (object == null) {
			return null;
		}

		JSONValue value = object.get(fieldName);

		if (value == null || value.isString() == null) {
			return null;
		}

		return value.isString().stringValue();
	}

	public static Long getLong(JSONObject object, String fieldName) {
		if (object == null) {
			return null;
		}

		JSONValue value = object.get(fieldName);

		if (value == null || value.isNumber() == null) {
			return null;
		}

		return (long) value.isNumber().doubleValue();
	}
}
