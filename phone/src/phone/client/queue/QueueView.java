package phone.client.queue;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class QueueView implements QueueDisplay {

	private FlowPanel panel;
	private Label queueLabel;
	private FlexTable queueTable;
	private FlowPanel queueContainer;
	private FlowPanel headPanel;

	public QueueView() {
		initQueue();
	}

	private void initQueue() {
		panel = new FlowPanel();

		headPanel = new FlowPanel();
		headPanel.setStyleName("header-panel");

		queueLabel = new Label("Очередь звонков:");
		headPanel.add(queueLabel);

		queueTable = new FlexTable();
		queueTable.setStyleName("queue-table");

		queueContainer = new FlowPanel();
		queueContainer.setStyleName("queue-box blue-background");
		queueContainer.add(queueTable);

		panel.add(headPanel);
		panel.add(queueContainer);
	}

	@Override
	public void clearQueue() {
		queueTable.removeAllRows();
	}

	@Override
	public void addNumber(String number) {
		int row = queueTable.getRowCount();

		queueTable.setText(row, 0, number.replaceFirst("8", "+7").replace("-", " "));
	}

	@Override
	public void pushQueue() {
		if (queueTable.getRowCount() > 0) {
			queueTable.removeRow(0);
		}
	}

	@Override
	public Widget asWidget() {
		return panel;
	}
}