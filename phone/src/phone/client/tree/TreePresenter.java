package phone.client.tree;

import com.google.gwt.user.client.ui.HasWidgets;

import phone.client.event.click.TreeButtonClickHandler;

public class TreePresenter {
	private TreeDisplay view;

	public TreePresenter(TreeDisplay view) {
		this.view = view;
	}
	
	public void bind() {
		view.setTreeButtonClickHandler(new TreeButtonClickHandler() {
			
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void go(HasWidgets container) {

		container.add(view.asWidget());

	}
}
