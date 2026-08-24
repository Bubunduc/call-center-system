package phone.client.currentNums;

import com.google.gwt.user.client.ui.Widget;

import phone.client.event.click.CurrentNumButtonClickHandler;

public interface CurrentNumsDisplay {
	
	void setCurrentNumButtonClickHandler(CurrentNumButtonClickHandler handler);
	Widget asWidget();
}
