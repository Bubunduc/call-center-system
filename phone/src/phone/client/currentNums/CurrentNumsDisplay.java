package phone.client.currentNums;

import com.google.gwt.user.client.ui.Widget;

import phone.client.event.click.CurrentNumButtonClickHandler;

public interface CurrentNumsDisplay {
	
	void setCurrentNumButtonClickHandler(CurrentNumButtonClickHandler handler);
	void addActiveCall(String id, String name,String phone);
	void colorSelectedRow(String id);
	Widget asWidget();
}
