package phone.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import phone.client.activeCalls.ActiveCallsDisplay;
import phone.client.activeCalls.ActiveCallsPresenter;
import phone.client.activeCalls.ActiveCallsView;
import phone.client.mainPanel.MainPanelDisplay;
import phone.client.mainPanel.MainPanelPresenter;
import phone.client.mainPanel.MainPanelView;
import phone.client.queue.QueueDisplay;
import phone.client.queue.QueuePresenter;
import phone.client.queue.QueueView;
import phone.client.request.ActiveCallsClient;
import phone.client.request.DeviceClient;
import phone.client.request.QueueClient;
import phone.client.request.RoomClient;
import phone.client.store.ClientPhoneStore;
import phone.client.tree.TreeDisplay;
import phone.client.tree.TreePresenter;
import phone.client.tree.TreeView;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Phone implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	// private final GreetingServiceAsync greetingService =
	// GWT.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		final ActiveCallsClient activeCallsClient = new ActiveCallsClient();
		final QueueClient queueClient = new QueueClient();
		final DeviceClient deviceClient = new DeviceClient();
		final RoomClient roomClient = new RoomClient();

		final TreeDisplay treeView = new TreeView();
		final TreePresenter treePresenter = new TreePresenter(treeView);

		final QueueDisplay queueView = new QueueView();
		final QueuePresenter queuePresenter = new QueuePresenter(queueView);

		final ActiveCallsDisplay activeCallsView = new ActiveCallsView();
		final ActiveCallsPresenter activeCallsPresenter = new ActiveCallsPresenter(activeCallsView);

		final ClientPhoneStore store = new ClientPhoneStore();

		final MainPanelDisplay mainPanelView = new MainPanelView(treeView, queueView, activeCallsView);
		final MainPanelPresenter mainPanelPresenter = MainPanelPresenter.builder()
				.activeCallsClient(activeCallsClient)
				.queueClient(queueClient)
				.deviceClient(deviceClient)
				.roomClient(roomClient)
				.treePresenter(treePresenter)
				.queuePresenter(queuePresenter)
				.activeCallsPresenter(activeCallsPresenter)
				.store(store)
				.view(mainPanelView)
				.build();
		mainPanelPresenter.go(RootPanel.get("mainContainer"));
	}
}
