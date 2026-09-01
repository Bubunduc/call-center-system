package phone.client.currentNums;

import com.google.gwt.user.client.ui.HasWidgets;

import phone.shared.dto.DeviceResponse;

public class CurrentNumsPresenter {
	
	CurrentNumsDisplay view;
	
	public CurrentNumsPresenter(CurrentNumsDisplay view) {
		
		this.view = view;
	}
	
	public void go(HasWidgets container) {

		container.add(view.asWidget());

	}
	
	public void loadData(DeviceResponse response) {
		
		view.addActiveCall(response.getDeviceNumber(),response.getOperatorName(),response.getIncomingNumber());
	}
	
	public void colorRow(String id) {
		view.colorSelectedRow(id);
	}
}
