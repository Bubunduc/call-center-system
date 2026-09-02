package phone.client.queue;

import java.util.List;

import phone.shared.dto.PhoneResponse;

public class QueuePresenter {
	
	private QueueDisplay view;
	
	public QueuePresenter(QueueDisplay view) {
		this.view = view;
	}
	
	public void loadData(List<PhoneResponse> data) {
		
		if (data == null) {
			return;
		}
		for(PhoneResponse number : data) {
			view.addNumber(number.getPhoneNumber());
		}
	}
	public void pushQueue() {
		view.pushQueue();
	}
	
	public void refreshQueue(List<PhoneResponse> data) {
		view.clearQueue();
		loadData(data);
	}
}
