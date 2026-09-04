package phone.client.queue;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class QueueView implements QueueDisplay {

	private FlowPanel panel;
	private Label queueLabel;
	private FlexTable queueTable;

	public QueueView() {
		initQueue();
	}

	private void initQueue() {
		panel = new FlowPanel();

		FlowPanel headPanel = new FlowPanel();
		headPanel.setStyleName("header-panel");

		queueLabel = new Label("Очередь звонков:");
		headPanel.add(queueLabel);

		queueTable = new FlexTable();
		queueTable.setStyleName("queue-box blue-background");

		panel.add(headPanel);
		panel.add(queueTable);
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