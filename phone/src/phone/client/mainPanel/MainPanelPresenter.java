package phone.client.mainPanel;

import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import phone.client.currentNums.CurrentNumsPresenter;
import phone.client.queue.QueuePresenter;
import phone.client.request.ActiveCallsClient;
import phone.client.request.DeviceClient;
import phone.client.request.QueueClient;
import phone.client.request.RoomClient;
import phone.client.tree.TreePresenter;
import phone.shared.dto.DeviceResponse;
import phone.shared.dto.PhoneResponse;
import phone.shared.dto.RoomResponse;

public class MainPanelPresenter {

	private final CurrentNumsPresenter currentNumsPresenter;
	private final QueuePresenter queuePresenter;
	private final TreePresenter treePresenter;
	private final MainPanelDisplay view;
	private final ActiveCallsClient activeCallsClient;
	private final QueueClient queueClient;
	private final RoomClient roomClient;
	private final DeviceClient deviceClient;

	private final String URL = "http://127.0.0.1:8888/api";

	public MainPanelPresenter(CurrentNumsPresenter currentNumsPresenter, QueuePresenter queuePresenter,
			TreePresenter treePresenter, MainPanelDisplay view, ActiveCallsClient activeCallsClient,
			QueueClient queueClient, RoomClient roomClient, DeviceClient deviceClient) {
		this.currentNumsPresenter = currentNumsPresenter;
		this.queuePresenter = queuePresenter;
		this.treePresenter = treePresenter;
		this.view = view;
		this.activeCallsClient = activeCallsClient;
		this.queueClient = queueClient;
		this.roomClient = roomClient;
		this.deviceClient = deviceClient;
		loadData();
	}

	public void go(HasWidgets container) {

		container.add(view.asWidget());

	}

	private void loadData() {

		queueClient.getQueue(URL, new AsyncCallback<List<PhoneResponse>>() {

			@Override
			public void onSuccess(List<PhoneResponse> result) {

				queuePresenter.loadData(result);

			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());

			}
		});
		roomClient.getRooms(URL, new AsyncCallback<List<RoomResponse>>() {
			@Override
			public void onSuccess(List<RoomResponse> result) {
				if (result != null && !result.isEmpty()) {

					loadDevicesForRoom(result, 0);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
		});

	}

	private void loadDevicesForRoom(final List<RoomResponse> rooms, final int index) {
		if (index >= rooms.size()) {
			return;
		}

		final RoomResponse currentRoom = rooms.get(index);

		deviceClient.getDevices(URL, currentRoom.getId(), new AsyncCallback<List<DeviceResponse>>() {
			@Override
			public void onSuccess(List<DeviceResponse> result) {
				treePresenter.loadNode(currentRoom, result);

				for (DeviceResponse i : result) {
					if (i.getIncomingNumber() != null) {
						currentNumsPresenter.loadData(i);
					}
				}

				loadDevicesForRoom(rooms, index + 1);
			}

			@Override
			public void onFailure(Throwable caught) {

				loadDevicesForRoom(rooms, index + 1);
			}
		});
	}

}
