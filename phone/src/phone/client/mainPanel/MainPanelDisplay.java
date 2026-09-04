package phone.client.mainPanel;

import com.google.gwt.user.client.ui.Widget;

import phone.client.event.click.ActiveCallsButtonClickHandler;
import phone.client.event.click.TreeButtonClickHandler;
import phone.client.event.select.ActiveCallsSelectionHandler;
import phone.client.event.select.TreeDeviceSelectionHandler;

public interface MainPanelDisplay {
	
	void setActiveCallsSelectionHandler(ActiveCallsSelectionHandler selectionHandler);

	void setActiveCallsButtonClickHandler(ActiveCallsButtonClickHandler handler);
	
	void setTreeSelectionHandler(TreeDeviceSelectionHandler selectionHandler);

	void setTreeButtonClickHandler(TreeButtonClickHandler handler);
	
	Widget asWidget();
}
