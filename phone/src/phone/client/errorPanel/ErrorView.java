package phone.client.errorPanel;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ErrorView implements ErrorDisplay {

	private FlowPanel panel;
	private Label errorMessage;

	public ErrorView() {
		init();
	}

	private void init() {
		panel = new FlowPanel();

		errorMessage = new Label();
		errorMessage.setStyleName("error-label");
		errorMessage.setVisible(false);

		panel.add(errorMessage);
	}

	@Override
	public void setErrorMessage(String message) {

		boolean hasError = message != null && !message.trim().isEmpty();

		if (hasError) {
			errorMessage.setText(message);
			errorMessage.setVisible(true);
		} else {
			errorMessage.setText("");
			errorMessage.setVisible(false);
		}
	}

	@Override
	public Widget asWidget() {
		return panel;
	}
}
