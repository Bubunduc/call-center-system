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

import phone.shared.dto.RoomResponse;

public class RoomClient {
private final String ROUTE = "/rooms";
	
	public void getRooms(String url, final AsyncCallback<List<RoomResponse>> callback) {

        RequestBuilder request = new RequestBuilder(RequestBuilder.GET, url + ROUTE);

        try {
            request.sendRequest(null, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
                        callback.onFailure(new Exception(response.getText()));
                        return;
                    }

                    try {
                        String jsonText = response.getText();
                        List<RoomResponse> resultList = parseJsonToDtoList(jsonText);

                        callback.onSuccess(resultList);

                    } catch (Exception e) {
                    	callback.onFailure(new Exception(response.getText()));
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
	
	private List<RoomResponse> parseJsonToDtoList(String jsonText) {
	    List<RoomResponse> list = new ArrayList<RoomResponse>();

	    if (jsonText == null || jsonText.trim().isEmpty()) {
	        return list;
	    }

	    JSONValue jsonValue = JSONParser.parseStrict(jsonText);
	    JSONArray jsonArray = jsonValue.isArray();

	    if (jsonArray != null) {
	        for (int i = 0; i < jsonArray.size(); i++) {
	        	
	            JSONObject object = jsonArray.get(i).isObject();
	            if (object != null && object.containsKey("id")) {
	            	
	                JSONValue idValue = object.get("id");
	                JSONValue nameValue = object.get("name");
	                
	                
	                if (idValue.isNumber() != null && nameValue.isString() != null) {
	                	Long id = (long) idValue.isNumber().doubleValue();
		                String name = nameValue.isString().stringValue();
		                list.add(new RoomResponse(id, name));
	                }
	            }
	        }
	    }
	    return list;
	}
}
