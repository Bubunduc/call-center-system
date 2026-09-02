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

import phone.shared.dto.ActiveCall;

public class ActiveCallsClient {

	private final String ROUTE = "/calls";
	
	public void getActiveCalls(String url,final AsyncCallback<List<ActiveCall>> callback) {

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
		changeStageReuqest(request, callback);
	}

	public void endCall(String url, String deviceNumber, final AsyncCallback<Void> callback) {
		String requestUrl = url + ROUTE + "?deviceNumber=" + deviceNumber;
		RequestBuilder request = new RequestBuilder(RequestBuilder.DELETE, requestUrl);
		changeStageReuqest(request, callback);
	}

	private void changeStageReuqest(RequestBuilder request, final AsyncCallback<Void> callback) {
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
//	private String deviceNumber;
//	private String operatorName;
//	private String phoneNumber;
	private List<ActiveCall> parseJsonToDtoList(String json) {
		List<ActiveCall> result = new ArrayList<ActiveCall>();

		JSONValue value = JSONParser.parseStrict(json);
		JSONArray array = value.isArray();

		if (array == null) {
			return result;
		}

		for (int i = 0; i < array.size(); i++) {
			JSONObject object = array.get(i).isObject();

			if (object == null) {
				continue;
			}

			String deviceNumber = object.get("deviceNumber").isString().stringValue();

			String operatorName = object.get("operatorName").isString().stringValue();

			String incomingNumber = object.get("phoneNumber").isString().stringValue();

			result.add(new ActiveCall(deviceNumber, operatorName, incomingNumber));
		}

		return result;
	}
}
