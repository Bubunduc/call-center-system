package phone.client.mainPanel;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import phone.client.activeCalls.ActiveCallsDisplay;
import phone.client.event.click.ActiveCallsButtonClickHandler;
import phone.client.event.click.TreeButtonClickHandler;
import phone.client.event.select.ActiveCallsSelectionHandler;
import phone.client.event.select.TreeDeviceSelectionHandler;
import phone.client.queue.QueueDisplay;
import phone.client.tree.TreeDisplay;

public class MainPanelView implements MainPanelDisplay {

	private FlowPanel mainPanel;
	private Label blueLabel;
	private FlowPanel upperRightPanel;
	private FlowPanel rightPanel;
	private final TreeDisplay treeView;
	private final ActiveCallsDisplay activeCallsView;

	public MainPanelView(TreeDisplay treeView, QueueDisplay queueView, ActiveCallsDisplay activeCallsView) {
		this.treeView = treeView;
		this.activeCallsView = activeCallsView;

		mainPanel = new FlowPanel();
		mainPanel.setStyleName("main-panel-flex");

		rightPanel = new FlowPanel();

		upperRightPanel = new FlowPanel();
		upperRightPanel.setStyleName("upper-right-panel");

		blueLabel = new Label("ТЕЛЕФОНИЯ - 3000");
		blueLabel.setStyleName("app-logo");

		upperRightPanel.add(queueView.asWidget());
		upperRightPanel.add(blueLabel);

		rightPanel.add(upperRightPanel);
		rightPanel.add(activeCallsView.asWidget());
		rightPanel.setStyleName("right-container");
		
		mainPanel.add(treeView.asWidget());
		mainPanel.add(rightPanel);
	}

	@Override
	public Widget asWidget() {

		return mainPanel;
	}

	@Override
	public void setActiveCallsSelectionHandler(ActiveCallsSelectionHandler selectionHandler) {
		activeCallsView.setCurrentNumSelectionHandler(selectionHandler);
		
	}

	@Override
	public void setActiveCallsButtonClickHandler(ActiveCallsButtonClickHandler handler) {
		activeCallsView.setCurrentNumButtonClickHandler(handler);
		
	}

	@Override
	public void setTreeSelectionHandler(TreeDeviceSelectionHandler selectionHandler) {
		treeView.setTreeSelectionHandler(selectionHandler);
		
	}

	@Override
	public void setTreeButtonClickHandler(TreeButtonClickHandler handler) {
		treeView.setTreeButtonClickHandler(handler);
		
	}

}
