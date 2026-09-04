package phone.client.activeCalls;

import com.google.gwt.user.client.ui.Widget;

import phone.client.event.click.ActiveCallsButtonClickHandler;
import phone.client.event.select.ActiveCallsSelectionHandler;

public interface ActiveCallsDisplay {

	void addActiveCall(String id, String name, String phone);
	
	void removeActiveCall(String id);

	void colorSelectedRow(String id);

	void setCurrentNumSelectionHandler(ActiveCallsSelectionHandler selectionHandler);

	void setCurrentNumButtonClickHandler(ActiveCallsButtonClickHandler handler);
	
	void clear();

	Widget asWidget();

}
