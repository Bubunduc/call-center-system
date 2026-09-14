package phone.client.errorPanel;

public class ErrorPresenter {
	private final ErrorDisplay view;
	
	public ErrorPresenter(ErrorDisplay view) {
		this.view = view;
	}
	
	public void setErrorMessage(String message) {
		view.setErrorMessage(message);
	}
}
