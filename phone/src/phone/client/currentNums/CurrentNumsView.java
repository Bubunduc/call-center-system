package phone.client.currentNums;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import phone.client.event.click.CurrentNumButtonClickHandler;

public class CurrentNumsView implements CurrentNumsDisplay {
    private FlowPanel mainPanel;
    private Label currentCalls;
    private Button endCall;
    private FlowPanel headPanel;
    private FlexTable numsTable;
    private CurrentNumButtonClickHandler buttonHandler;
    
    public CurrentNumsView() {
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
}