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

import phone.shared.dto.PhoneResponse;

public class QueueClient {
	
	private final String ROUTE = "/queue";
	
	public void getQueue(String url, final AsyncCallback<List<PhoneResponse>> callback) {

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
                        
                        List<PhoneResponse> resultList = parseJsonToDtoList(jsonText);

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
	
	private List<PhoneResponse> parseJsonToDtoList(String jsonText) {
	    List<PhoneResponse> list = new ArrayList<PhoneResponse>();

	    if (jsonText == null || jsonText.trim().isEmpty()) {
	        return list;
	    }

	    JSONValue jsonValue = JSONParser.parseStrict(jsonText);
	    JSONArray jsonArray = jsonValue.isArray();

	    if (jsonArray != null) {
	        for (int i = 0; i < jsonArray.size(); i++) {
	        	
	            JSONObject object = jsonArray.get(i).isObject();
	            if (object != null && object.containsKey("phoneNumber")) {
	            	
	                JSONValue value = object.get("phoneNumber");
	                if (value.isString() != null) {
	                    list.add(new PhoneResponse(value.isString().stringValue()));
	                }
	            }
	        }
	    }
	    return list;
	}
}
