package phone.client.currentNums;

import com.google.gwt.user.client.ui.Widget;

import phone.client.event.click.CurrentNumButtonClickHandler;
import phone.client.event.select.ActiveCallSelectionHandler;

public interface CurrentNumsDisplay {

	void addActiveCall(String id, String name, String phone);
	
	void removeActiveCall(String id);

	void colorSelectedRow(String id);

	void setCurrentNumSelectionHandler(ActiveCallSelectionHandler selectionHandler);

	void setCurrentNumButtonClickHandler(CurrentNumButtonClickHandler handler);
	
	void clear();

	Widget asWidget();

}
