package phone.client.mainPanel;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import phone.client.activeCalls.ActiveCallsPresenter;
import phone.client.dto.DeviceInfo;
import phone.client.errorPanel.ErrorPresenter;
import phone.client.event.click.ActiveCallsButtonClickHandler;
import phone.client.event.click.TreeButtonClickHandler;
import phone.client.event.select.ActiveCallsSelectionHandler;
import phone.client.event.select.TreeDeviceSelectionHandler;
import phone.client.queue.QueuePresenter;
import phone.client.request.ActiveCallsClient;
import phone.client.request.DeviceClient;
import phone.client.request.QueueClient;
import phone.client.request.RoomClient;
import phone.client.store.ClientPhoneStore;
import phone.client.tree.TreePresenter;
import phone.shared.dto.ActiveCall;
import phone.shared.dto.DeviceResponse;
import phone.shared.dto.PhoneResponse;
import phone.shared.dto.RoomResponse;

public class MainPanelPresenter {

	private final ActiveCallsPresenter activeCallsPresenter;
	private final QueuePresenter queuePresenter;
	private final TreePresenter treePresenter;
	private final MainPanelDisplay view;
	private final ActiveCallsClient activeCallsClient;
	private final QueueClient queueClient;
	private final RoomClient roomClient;
	private final DeviceClient deviceClient;
	private final ClientPhoneStore store;
	private final ErrorPresenter errorPresenter;

	private Timer refreshTimer;
	private final String URL = GWT.getHostPageBaseURL() + "api";
	private boolean pollingError = false; // true, только если текущая ошибка вызвана поллингом

	public MainPanelPresenter(Builder builder) {
		this.activeCallsPresenter = builder.activeCallsPresenter;
		this.queuePresenter = builder.queuePresenter;
		this.treePresenter = builder.treePresenter;
		this.view = builder.view;
		this.activeCallsClient = builder.activeCallsClient;
		this.queueClient = builder.queueClient;
		this.roomClient = builder.roomClient;
		this.deviceClient = builder.deviceClient;
		this.errorPresenter = builder.errorPresenter;
		this.store = builder.store;
		loadData();
		bind();
		startPolling();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private ActiveCallsPresenter activeCallsPresenter;
		private QueuePresenter queuePresenter;
		private TreePresenter treePresenter;
		private MainPanelDisplay view;
		private ActiveCallsClient activeCallsClient;
		private QueueClient queueClient;
		private RoomClient roomClient;
		private DeviceClient deviceClient;
		private ClientPhoneStore store;
		private ErrorPresenter errorPresenter;

		public Builder activeCallsPresenter(ActiveCallsPresenter activeCallsPresenter) {
			this.activeCallsPresenter = activeCallsPresenter;
			return this;
		}

		public Builder queuePresenter(QueuePresenter queuePresenter) {
			this.queuePresenter = queuePresenter;
			return this;
		}

		public Builder treePresenter(TreePresenter treePresenter) {
			this.treePresenter = treePresenter;
			return this;
		}

		public Builder view(MainPanelDisplay view) {
			this.view = view;
			return this;
		}

		public Builder activeCallsClient(ActiveCallsClient activeCallsClient) {
			this.activeCallsClient = activeCallsClient;
			return this;
		}

		public Builder queueClient(QueueClient queueClient) {
			this.queueClient = queueClient;
			return this;
		}

		public Builder roomClient(RoomClient roomClient) {
			this.roomClient = roomClient;
			return this;
		}

		public Builder deviceClient(DeviceClient deviceClient) {
			this.deviceClient = deviceClient;
			return this;
		}

		public Builder errorPresenter(ErrorPresenter errorPresenter) {
			this.errorPresenter = errorPresenter;
			return this;
		}

		public Builder store(ClientPhoneStore store) {
			this.store = store;
			return this;
		}

		public MainPanelPresenter build() {
			if (activeCallsPresenter == null 
					|| queuePresenter == null
					|| treePresenter == null
					|| errorPresenter == null 
					|| view == null 
					|| activeCallsClient == null 
					|| queueClient == null
					|| roomClient == null 
					|| deviceClient == null 
					|| store == null) {

				throw new IllegalStateException("Не заданы все зависимости MainPanelPresenter");
			}

			return new MainPanelPresenter(this);
		}

	}

	private void bind() {
		bindActiveCalls();
		bindTree();
	}

	public void go(HasWidgets container) {

		container.add(view.asWidget());

	}

	private void loadData() {

		queueClient.getQueue(URL, new AsyncCallback<List<PhoneResponse>>() {

			@Override
			public void onSuccess(List<PhoneResponse> result) {

				store.addToQueueList(result);
				queuePresenter.loadData(result);
				errorPresenter.clearErrorMessage();
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Ошибка обновления активных звонков", caught);
				errorPresenter.setErrorMessage(caught.getMessage());

			}
		});
		roomClient.getRooms(URL, new AsyncCallback<List<RoomResponse>>() {
			@Override
			public void onSuccess(List<RoomResponse> result) {
				if (result != null && !result.isEmpty()) {

					loadDevicesForRoom(result, 0);
				}
				errorPresenter.clearErrorMessage();
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Ошибка обновления активных звонков", caught);
				errorPresenter.setErrorMessage(caught.getMessage());
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
					store.addDevice(new DeviceInfo(i.getDeviceNumber(), i.getOperatorName()));
					if (i.getIncomingNumber() != null) {
						store.addActiveCall(new ActiveCall(
							i.getDeviceNumber(),
							i.getOperatorName(),
							i.getIncomingNumber()));
						
						activeCallsPresenter.loadData(i);
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

	private void bindActiveCalls() {
		view.setActiveCallsButtonClickHandler(new ActiveCallsButtonClickHandler() {

			@Override
			public void onClick() {
				final String selectedCallId = store.getSelectedActiveCallId();
				if (selectedCallId == null) {
					errorPresenter.setErrorMessage("Сначала выберите звонок");
					return;
				}
				activeCallsClient.endCall(URL, selectedCallId, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						activeCallsPresenter.removeActiveCall(selectedCallId);
						store.removeActiveCall(selectedCallId);
						store.setSelectedActiveCallId(null);
						errorPresenter.clearErrorMessage();
					}

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Ошибка обновления активных звонков", caught);
						errorPresenter.setErrorMessage(caught.getMessage());

					}
				});

			}
		});
		view.setActiveCallsSelectionHandler(new ActiveCallsSelectionHandler() {

			@Override
			public void onSelected(String id) {
				store.setSelectedActiveCallId(id);
				activeCallsPresenter.colorRow(id);
			}
		});
	}

	private void bindTree() {
		view.setTreeButtonClickHandler(new TreeButtonClickHandler() {

			@Override
			public void onClick() {
				final String number = store.getNext();
				if (number == null) {
					errorPresenter.setErrorMessage("Очередь звонков пуста");
					return;
				}
				final DeviceInfo selectedDevice = store.getSelectedDevice();
				if (selectedDevice == null) {
					errorPresenter.setErrorMessage("Сначала выберите устройство");
					return;
				}
				if (store.isDeviceBusy(selectedDevice.getId())) {
					errorPresenter.setErrorMessage("Текущий оператор уже с кем то разговаривает");
					return;
				}
				activeCallsClient.acceptCall(URL, selectedDevice.getId(), number, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {

						store.pushQueue();
						queuePresenter.pushQueue();

						ActiveCall newCall = new ActiveCall(
								selectedDevice.getId(), 
								selectedDevice.getOperatorName(),
								number);

						store.addActiveCall(newCall);
						activeCallsPresenter.addActiveCall(newCall);
						treePresenter.uncolorNode(selectedDevice.getId());
						store.setSelectedTreeDeviceId(null);
						errorPresenter.clearErrorMessage();
					}

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Ошибка обновления активных звонков", caught);
						errorPresenter.setErrorMessage(caught.getMessage());

					}
				});

			}
		});
		view.setTreeSelectionHandler(new TreeDeviceSelectionHandler() {

			@Override
			public void onSelected(String id) {
				String prevId = store.getSelectedTreeDeviceId();
				if (prevId != null) {
					treePresenter.uncolorNode(prevId);
				}
				store.setSelectedTreeDeviceId(id);
				treePresenter.colorNode(id);

			}
		});
	}

	private void startPolling() {
		refreshTimer = new Timer() {

			@Override
			public void run() {
				refreshQueue();
				refreshActiveCalls();

			}
		};

		refreshTimer.scheduleRepeating(500);
	}

	private void refreshQueue() {
		queueClient.getQueue(URL, new AsyncCallback<List<PhoneResponse>>() {

			@Override
			public void onSuccess(List<PhoneResponse> result) {

				if (store.updateQueue(result)) {
					queuePresenter.refreshQueue(result);
				}
				if (pollingError) {
					errorPresenter.clearErrorMessage();
					pollingError = false;
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Ошибка обновления активных звонков", caught);
				pollingError = true;
				errorPresenter.setErrorMessage(caught.getMessage());

			}
		});
	}

	private void refreshActiveCalls() {
		activeCallsClient.getActiveCalls(URL, new AsyncCallback<List<ActiveCall>>() {

			@Override
			public void onSuccess(List<ActiveCall> result) {
				if (store.updateActiveCalls(result)) {
					activeCallsPresenter.refreshData(result);
					if (store.getSelectedActiveCallId() != null) {
						activeCallsPresenter.colorRow(store.getSelectedActiveCallId());
					}
					if (pollingError) {
						errorPresenter.clearErrorMessage();
						pollingError = false;
					}
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Ошибка обновления активных звонков", caught);
				pollingError = true;
				errorPresenter.setErrorMessage(caught.getMessage());

			}
		});
	}

}
