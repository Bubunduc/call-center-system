package phone.client.request;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

import phone.client.request.utils.JsonUtils;
import phone.shared.dto.DeviceResponse;

public class DeviceClient {
	private final String ROUTE = "/rooms/devices";

	public void getDevices(String url, Long id, final AsyncCallback<List<DeviceResponse>> callback) {

		RequestBuilder request = new RequestBuilder(RequestBuilder.GET, url + ROUTE + "?roomId=" + String.valueOf(id));

		try {
			request.sendRequest(null, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
						callback.onFailure(new Exception("Ошибка сервера, код ответа: " + response.getStatusCode()));
						return;
					}

					try {
						String jsonText = response.getText();
						List<DeviceResponse> resultList = parseJsonToDtoList(jsonText);

						callback.onSuccess(resultList);

					} catch (Exception e) {
						callback.onFailure(new Exception("Ошибка парсинга JSON: " + e.getMessage(), e));
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
					callback.onFailure(exception);
				}
			});
		} catch (RequestException e) {
			callback.onFailure(e);
		}
	}

//	private String deviceNumber;
//	private String operatorName;
//	private String incomingNumber;

	private List<DeviceResponse> parseJsonToDtoList(String jsonText) {
		List<DeviceResponse> list = new ArrayList<DeviceResponse>();

		if (jsonText == null || jsonText.trim().isEmpty()) {
			return list;
		}

		JSONValue jsonValue = JSONParser.parseStrict(jsonText);
		JSONArray jsonArray = jsonValue.isArray();

		if (jsonArray == null) {
			return list;
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject object = jsonArray.get(i).isObject();

			if (object == null) {
				continue;
			}

			String deviceNumber = JsonUtils.getString(object, "deviceNumber");

			String operatorName = JsonUtils.getString(object, "operatorName");

			String incomingNumber = JsonUtils.getString(object, "incomingNumber");

			if (deviceNumber == null || operatorName == null) {
				continue;
			}

			list.add(new DeviceResponse(deviceNumber, operatorName, incomingNumber));
		}

		return list;
	}
}
