package phone.client.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import phone.client.event.click.TreeButtonClickHandler;
import phone.client.event.select.TreeDeviceSelectionHandler;
import phone.shared.dto.DeviceResponse;
import phone.shared.dto.RoomResponse;

public class TreeView implements TreeDisplay {

	private FlowPanel mainPanel;
	private FlowPanel treePanel;
	private Label roomsLablel;
	private Button responseButton;
	private FlowPanel headerPanel;
	private TreeButtonClickHandler buttonHandler;
	private TreeDeviceSelectionHandler selectionHandler;

	private final Map<String, FlowPanel> deviceMap;

	public TreeView() {
		deviceMap = new HashMap<String, FlowPanel>();
		initTree();
	}

	private void initTree() {
		mainPanel = new FlowPanel();
		treePanel = new FlowPanel();

		treePanel.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				handleTreeClick(event);
			}
		}, ClickEvent.getType());

		roomsLablel = new Label("Комнаты:");

		headerPanel = new FlowPanel();
		headerPanel.setStyleName("tree-header");

		responseButton = new Button("Ответить");
		responseButton.setStyleName("response-button blue-background");

		responseButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (buttonHandler != null) {
					buttonHandler.onClick();
				}
			}
		});

		headerPanel.add(roomsLablel);
		headerPanel.add(responseButton);

		mainPanel.add(headerPanel);
		mainPanel.add(treePanel);

		treePanel.setStyleName("tree-panel gray-background");
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public void setTreeButtonClickHandler(TreeButtonClickHandler handler) {
		this.buttonHandler = handler;

	}

	@Override
	public void setTreeSelectionHandler(TreeDeviceSelectionHandler selectionHandler) {
		this.selectionHandler = selectionHandler;
	}

	@Override
	public void showNode(RoomResponse room, List<DeviceResponse> devices) {
		FlowPanel roomPanel = new FlowPanel();
		Label roomName = new Label(room.getName());
		roomPanel.add(roomName);

		roomPanel.getElement().setAttribute("room-id", room.getId().toString());

		for (DeviceResponse device : devices) {
			FlowPanel nodePanel = new FlowPanel();
			Label nodeName = new Label(device.getDeviceNumber() + " " + device.getOperatorName());

			nodePanel.getElement().setAttribute("room-device-id", device.getDeviceNumber());
			nodePanel.setStyleName("node-panel");

			nodePanel.add(nodeName);

			roomPanel.add(nodePanel);

			deviceMap.put(device.getDeviceNumber(), nodePanel);

		}

		treePanel.add(roomPanel);

	}

	@Override
	public void colorNode(String deviceId) {
		if (deviceId == null || deviceId.isEmpty()) {
			return;
		}
		deviceMap.get(deviceId).addStyleName("selected-row");
	}

	@Override
	public void uncolorNode(String deviceId) {
		if (deviceId == null || deviceId.isEmpty()) {
			return;
		}
		deviceMap.get(deviceId).removeStyleName("selected-row");
	}

	private void handleTreeClick(ClickEvent event) {

		Element clickedElement = event.getNativeEvent().getEventTarget().cast();

		Element nodeElement = clickedElement.getParentElement();

		String idValue = nodeElement.getAttribute("room-device-id");
		if ((idValue == null) || (idValue.isEmpty())) {
			return;
		}

		FlowPanel device = deviceMap.get(idValue);
		if (device == null) {
			return;
		}

		selectionHandler.onSelected(idValue);
	}

}
