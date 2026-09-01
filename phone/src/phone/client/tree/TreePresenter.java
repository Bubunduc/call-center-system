package phone.client.tree;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;

import phone.client.event.click.TreeButtonClickHandler;
import phone.shared.dto.DeviceResponse;
import phone.shared.dto.RoomResponse;

public class TreePresenter {
	private TreeDisplay view;

	public TreePresenter(TreeDisplay view) {
		this.view = view;
	}

	public void go(HasWidgets container) {

		container.add(view.asWidget());

	}
	
	public void loadNode(RoomResponse room,List<DeviceResponse> devices) {
		view.showNode(room,devices);
	}
	
	public void colorNode(String id) {
		view.colorNode(id);
	}
	
	public void uncolorNode(String id) {
		view.uncolorNode(id);
	}
}
