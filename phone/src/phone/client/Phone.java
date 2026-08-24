package phone.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import phone.client.currentNums.CurrentNumsDisplay;
import phone.client.currentNums.CurrentNumsPresenter;
import phone.client.currentNums.CurrentNumsView;
import phone.client.mainPanel.MainPanelDisplay;
import phone.client.mainPanel.MainPanelPresenter;
import phone.client.mainPanel.MainPanelView;
import phone.client.queue.QueueDisplay;
import phone.client.queue.QueuePresenter;
import phone.client.queue.QueueView;
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

		TreeDisplay treeView = new TreeView();
		TreePresenter treePresenter = new TreePresenter(treeView);

		QueueDisplay queueView = new QueueView();
		QueuePresenter queuePresenter = new QueuePresenter(queueView);

		CurrentNumsDisplay currentNumsView = new CurrentNumsView();
		CurrentNumsPresenter currentNumsPresenter = new CurrentNumsPresenter(currentNumsView);

		MainPanelDisplay mainPanelView = new MainPanelView(treeView, queueView, currentNumsView);
		MainPanelPresenter mainPanelPresenter = new MainPanelPresenter(currentNumsPresenter, queuePresenter,
				treePresenter, mainPanelView);
		mainPanelPresenter.go(RootPanel.get("mainContainer"));
	}
}
