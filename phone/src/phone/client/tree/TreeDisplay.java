package phone.client.tree;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;

import phone.client.event.click.TreeButtonClickHandler;
import phone.shared.dto.DeviceResponse;
import phone.shared.dto.RoomResponse;

public interface TreeDisplay {
	
	void setTreeButtonClickHandler(TreeButtonClickHandler handler);
	
	Widget asWidget();

	void showNode(RoomResponse room, List<DeviceResponse> devices);
}
