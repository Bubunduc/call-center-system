package phone.client.tree;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;

import phone.client.event.click.TreeButtonClickHandler;
import phone.client.event.select.TreeDeviceSelectionHandler;
import phone.shared.dto.DeviceResponse;
import phone.shared.dto.RoomResponse;

public interface TreeDisplay {
	Widget asWidget();

	void showNode(RoomResponse room, List<DeviceResponse> devices);

	void colorNode(String deviceId);

	void uncolorNode(String deviceId);

	void setTreeSelectionHandler(TreeDeviceSelectionHandler selectionHandler);

	void setTreeButtonClickHandler(TreeButtonClickHandler handler);
}
