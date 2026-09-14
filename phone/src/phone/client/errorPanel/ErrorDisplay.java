package phone.client.errorPanel;

import com.google.gwt.user.client.ui.Widget;

public interface ErrorDisplay {

	void setErrorMessage(String message);

	Widget asWidget();

}
