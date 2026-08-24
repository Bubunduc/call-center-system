package phone.client.tree;

import com.google.gwt.user.client.ui.Widget;

import phone.client.event.click.TreeButtonClickHandler;

public interface TreeDisplay {
	
	void setTreeButtonClickHandler(TreeButtonClickHandler handler);
	
	Widget asWidget();
}
