package phone.client.currentNums;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import phone.client.event.click.CurrentNumButtonClickHandler;
import phone.client.event.select.ActiveCallSelectionHandler;

public class CurrentNumsView implements CurrentNumsDisplay {
	private FlowPanel mainPanel;
	private Label currentCalls;
	private Button endCall;
	private FlowPanel headPanel;
	private FlexTable numsTable;
	private ActiveCallSelectionHandler selectionHandler;
	private CurrentNumButtonClickHandler buttonHandler;

	private final Map<Integer, String> rowToDeviceId;

	public CurrentNumsView() {
		rowToDeviceId = new HashMap<Integer, String>();
		initPanel();
	}

	private void initPanel() {
		mainPanel = new FlowPanel();
		mainPanel.setStyleName("active-calls-container");

		headPanel = new FlowPanel();
		headPanel.setStyleName("header-panel");

		currentCalls = new Label("Текущие звонки:");

		endCall = new Button("Окончить");
		endCall.setStyleName("response-button blue-background btn-answer");
		endCall.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (buttonHandler != null) {
					buttonHandler.onClick();
				}
			}
		});

		numsTable = new FlexTable();
		numsTable.setStyleName("active-calls-box gray-background");
		numsTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				HTMLTable.Cell cell = numsTable.getCellForEvent(event);

				if (cell == null) {
					return;
				}

				int rowIndex = cell.getRowIndex();

				String deviceId = rowToDeviceId.get(rowIndex);

				if (deviceId == null) {
					return;
				}

				selectionHandler.onSelected(deviceId);

			}
		});
		
		numsTable.setCellPadding(0);
		numsTable.setCellSpacing(0);
		numsTable.setBorderWidth(0);
		
		headPanel.add(currentCalls);
		headPanel.add(endCall);

		mainPanel.add(headPanel);
		mainPanel.add(numsTable);
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public void setCurrentNumButtonClickHandler(CurrentNumButtonClickHandler handler) {
		this.buttonHandler = handler;
	}
	
	@Override
	public void setCurrentNumSelectionHandler(ActiveCallSelectionHandler selectionHandler) {
		this.selectionHandler = selectionHandler;
		
	}
	

	@Override
	public void addActiveCall(String id, String name, String phone) {
		Integer lastRow = numsTable.getRowCount();

		numsTable.setText(lastRow, 0, id);
		numsTable.setText(lastRow, 1, name);
		numsTable.setText(lastRow, 2, phone);

		rowToDeviceId.put(lastRow, id);
	}
	
	@Override
	public void removeActiveCall(String id) {
	    if (id == null || id.isEmpty()) {
	        return;
	    }
	    Integer targetRow = null;
	    for (Map.Entry<Integer, String> entry : rowToDeviceId.entrySet()) {
	        if (id.equals(entry.getValue())) {
	            targetRow = entry.getKey();
	            break;
	        }
	    }

	    if (targetRow == null) {
	        return;
	    }

	    numsTable.removeRow(targetRow);
	    rebuildRowToDeviceIdMap();
	}

	private void rebuildRowToDeviceIdMap() {
	    rowToDeviceId.clear();
	    int rowCount = numsTable.getRowCount();

	    for (int row = 0; row < rowCount; row++) {
	        String deviceId = numsTable.getText(row, 0);
	        rowToDeviceId.put(row, deviceId);
	    }
	}
	
	@Override
	public void colorSelectedRow(String id) {

		for (Map.Entry<Integer, String> entry : rowToDeviceId.entrySet()) {
			
			int rowIndex = entry.getKey();
			String deviceId = entry.getValue();
			
			
			int cellCount = numsTable.getCellCount(rowIndex);

			for (int column = 0; column < cellCount; column++) {

				if (id != null && id.equals(deviceId)) {

					numsTable.getCellFormatter().addStyleName(rowIndex, column, "selected-row");

				} else {

					numsTable.getCellFormatter().removeStyleName(rowIndex, column, "selected-row");
				}
			}
		}
	}

	

}