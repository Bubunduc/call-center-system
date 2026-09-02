package phone.client.request;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

import phone.shared.dto.ActiveCall;
import phone.shared.dto.PhoneResponse;

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
	/**
	 * Подписка на SSE поток
	 * @return JavaScriptObject (ссылка на EventSource), чтобы можно было вызвать stopSseStream()
	 */
	public JavaScriptObject subscribeToQueueStream(String url, final AsyncCallback<List<ActiveCall>> callback) {
		return createSseStream(url + ROUTE, callback);
	}

	/**
	 * Закрыть SSE соединение
	 */
	public native void stopSseStream(JavaScriptObject eventSource) /*-{
		if (eventSource) {
			eventSource.close();
		}
	}-*/;

	private native JavaScriptObject createSseStream(String url, AsyncCallback<List<ActiveCall>> callback) /*-{
		var self = this;
		var source = new EventSource(url);

		source.onmessage = $entry(function(event) {
			try {
				var jsonText = event.data;
				// Вызываем наш Java метод парсинга
				var resultList = self.@phone.client.request.QueueClient::parseJsonToDtoList(Ljava/lang/String;)(jsonText);
				callback.@com.google.gwt.user.client.rpc.AsyncCallback::onSuccess(Ljava/lang/Object;)(resultList);
			} catch (e) {
				var exception = @java.lang.Exception::new(Ljava/lang/String;)("Ошибка парсинга SSE: " + e);
				callback.@com.google.gwt.user.client.rpc.AsyncCallback::onFailure(Ljava/lang/Throwable;)(exception);
			}
		});

		source.onerror = $entry(function(event) {
			var exception = @java.lang.Exception::new(Ljava/lang/String;)("Ошибка SSE соединения");
			callback.@com.google.gwt.user.client.rpc.AsyncCallback::onFailure(Ljava/lang/Throwable;)(exception);
		});

		return source;
	}-*/;
}
