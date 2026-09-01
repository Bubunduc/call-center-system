package phone.client.queue;

import com.google.gwt.user.client.ui.Widget;

public interface QueueDisplay {
	
	void clearQueue();
	
	void addNumber(String number);
	
	void pushQueue();
	
	Widget asWidget();
}
