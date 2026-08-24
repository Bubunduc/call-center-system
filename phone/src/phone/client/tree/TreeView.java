package phone.client.tree;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import phone.client.event.click.TreeButtonClickHandler;

public class TreeView implements TreeDisplay {

	private FlowPanel mainPanel;
	private FlowPanel treePanel;
	private Label roomsLablel;
	private Button responseButton;
	private FlowPanel headerPanel;
	private TreeButtonClickHandler buttonHandler;

	public TreeView() {
		initTree();
	}

	private void initTree() {
	    mainPanel = new FlowPanel();
	    treePanel = new FlowPanel();
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

}
