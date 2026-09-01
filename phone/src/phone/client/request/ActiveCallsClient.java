package phone.client.request;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ActiveCallsClient {

	private final String ROUTE = "/calls";

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
}
