package phone.client.currentNums;

import com.google.gwt.user.client.ui.HasWidgets;

public class CurrentNumsPresenter {
	
	CurrentNumsDisplay view;
	
	public CurrentNumsPresenter(CurrentNumsDisplay view) {
		
		this.view = view;
	}
	
	public void go(HasWidgets container) {

		container.add(view.asWidget());

	}
}
