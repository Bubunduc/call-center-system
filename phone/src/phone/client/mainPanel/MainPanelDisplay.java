package phone.client.mainPanel;

import com.google.gwt.user.client.ui.Widget;

import phone.client.event.click.CurrentNumButtonClickHandler;
import phone.client.event.click.TreeButtonClickHandler;
import phone.client.event.select.ActiveCallSelectionHandler;
import phone.client.event.select.TreeDeviceSelectionHandler;

public interface MainPanelDisplay {
	
	void setCurrentNumSelectionHandler(ActiveCallSelectionHandler selectionHandler);

	void setCurrentNumButtonClickHandler(CurrentNumButtonClickHandler handler);
	
	void setTreeSelectionHandler(TreeDeviceSelectionHandler selectionHandler);

	void setTreeButtonClickHandler(TreeButtonClickHandler handler);
	
	Widget asWidget();
}
