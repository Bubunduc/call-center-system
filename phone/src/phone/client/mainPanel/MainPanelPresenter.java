package phone.client.mainPanel;

import com.google.gwt.user.client.ui.HasWidgets;

import phone.client.currentNums.CurrentNumsPresenter;
import phone.client.queue.QueuePresenter;
import phone.client.tree.TreePresenter;

public class MainPanelPresenter {
	private final CurrentNumsPresenter currentNumsPresenter;
	private final QueuePresenter queuePresenter;
	private final TreePresenter treePresenter;
	private final MainPanelDisplay view;
	
	public MainPanelPresenter(CurrentNumsPresenter currentNumsPresenter, QueuePresenter queuePresenter,
			TreePresenter treePresenter,MainPanelDisplay view) {
		this.currentNumsPresenter = currentNumsPresenter;
		this.queuePresenter = queuePresenter;
		this.treePresenter = treePresenter;
		this.view = view;
	}
	
	public void go(HasWidgets container) {

		container.add(view.asWidget());

	}
	
	
	
}
