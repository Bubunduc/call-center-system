package phone.client.errorPanel;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ErrorView implements ErrorDisplay{
	private FlowPanel panel;
	private Label errorMessage;
	public ErrorView() {
		init();
	}
	private void init() {
		panel = new FlowPanel();
		errorMessage = new Label();
		errorMessage.setStyleName("error-label");
		
		panel.add(errorMessage);
	}
	
	@Override
	public void setErrorMessage(String message) {
		if (message == null) {
			errorMessage.setText("");
			return;
		}
		errorMessage.setText(message);
		
	} 
	@Override
	public Widget asWidget() {
		return panel;
	}
	
}
