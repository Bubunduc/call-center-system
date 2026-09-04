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
import com.google.gwt.user.client.rpc.AsyncCallback;

import phone.client.request.utils.JsonUtils;
import phone.shared.dto.ActiveCall;

public class ActiveCallsClient {

	private final String ROUTE = "/calls";

	public void getActiveCalls(String url, final AsyncCallback<List<ActiveCall>> callback) {

		RequestBuilder request = new RequestBuilder(RequestBuilder.GET, url + ROUTE);

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
						List<ActiveCall> resultList = parseJsonToDtoList(jsonText);

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

	public void acceptCall(String url, String deviceNumber, String phoneNumber, final AsyncCallback<Void> callback) {
		String requestUrl = url + ROUTE + "?deviceNumber=" + deviceNumber + "&phoneNumber=" + phoneNumber;
		RequestBuilder request = new RequestBuilder(RequestBuilder.POST, requestUrl);
		changeStageRequest(request, callback);
	}

	public void endCall(String url, String deviceNumber, final AsyncCallback<Void> callback) {
		String requestUrl = url + ROUTE + "?deviceNumber=" + deviceNumber;
		RequestBuilder request = new RequestBuilder(RequestBuilder.DELETE, requestUrl);
		changeStageRequest(request, callback);
	}

	private void changeStageRequest(RequestBuilder request, final AsyncCallback<Void> callback) {
		try {
			request.sendRequest(null, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
						callback.onFailure(new Exception(response.getText() + "\n" + response.getStatusCode()));
						return;
					}

					callback.onSuccess(null);
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

	private List<ActiveCall> parseJsonToDtoList(String jsonText) {
		List<ActiveCall> list = new ArrayList<ActiveCall>();

		JSONArray jsonArray = JsonUtils.parseArray(jsonText);
		
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

			String phoneNumber = JsonUtils.getString(object, "phoneNumber");

			if (deviceNumber == null || operatorName == null || phoneNumber == null) {
				continue;
			}

			list.add(new ActiveCall(deviceNumber, operatorName, phoneNumber));
		}

		return list;
	}
}
