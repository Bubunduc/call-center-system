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

		ActiveCallsClient activeCallsClient = new ActiveCallsClient();
		QueueClient queueClient = new QueueClient();
		DeviceClient deviceClient = new DeviceClient();
		RoomClient roomClient = new RoomClient();
		
		TreeDisplay treeView = new TreeView();
		TreePresenter treePresenter = new TreePresenter(treeView);

		QueueDisplay queueView = new QueueView();
		QueuePresenter queuePresenter = new QueuePresenter(queueView);

		ActiveCallsDisplay currentNumsView = new ActiveCallsView();
		ActiveCallsPresenter currentNumsPresenter = new ActiveCallsPresenter(currentNumsView);
		
		ClientPhoneStore store = new ClientPhoneStore();
		
		MainPanelDisplay mainPanelView = new MainPanelView(treeView, queueView, currentNumsView);
		MainPanelPresenter mainPanelPresenter = new MainPanelPresenter(currentNumsPresenter, queuePresenter,
				treePresenter, mainPanelView, activeCallsClient, queueClient, roomClient, deviceClient, store);
		mainPanelPresenter.go(RootPanel.get("mainContainer"));
	}
}
