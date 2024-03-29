package de.rowbuddy.client.menu;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.rowbuddy.client.menu.MenuPresenter.MenuDisplay;

public class MenuView extends Composite implements MenuDisplay {

	// TODO: Neue Fahrt, Nachtrag
	private DecoratedStackPanel menuPanel = new DecoratedStackPanel();

	private List<MenuItem> menuItems;

	public MenuView() {

		// initWidget(stackPanel);
		VerticalPanel verticalPanel = new VerticalPanel();

		verticalPanel.add(menuPanel);
		initWidget(verticalPanel);
	}

	private void renderMenuItems() {
		for (final MenuItem m : menuItems) {
			final FlexTable tb1 = new FlexTable();
			for (int i = 0; i < m.getSubItems().size(); i++) {
				final int h = i;
				final Anchor toAdd = new Anchor(m.getSubItems().get(i).getTitle());
				tb1.setWidget(i, 0, toAdd);
				if(m.getSubItems().get(i).getAssociatedEvent() != null){
					tb1.getRowFormatter().setStyleName(i, "menuItem");
				} else {
					tb1.getWidget(i, 0).setStyleName("crossedMenuItem");
					tb1.getRowFormatter().setStyleName(i, "crossedMenuItem");
				}

				toAdd.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent arg0) {
						m.menuSubItemClicked(m.getSubItems().get(h));
					}
				});
			}
			if (m.getSubItems().size() == 0 && m.getAssociatedEvent() != null) { // if an item has no children and needs to be clickable
				menuPanel.addHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent arg0) {
						if(menuPanel.getSelectedIndex() == m.getId()){ // ID MUST equivalent to the order number it has been added within the menuPanel
							NativeEvent event = Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false);
							ClickEvent.fireNativeEvent(event, tb1);
							m.menuItemClicked(m);
						}
					}
				},ClickEvent.getType());
			}
			String panel = getHeaderString(m.getTitle(), m.getImage(),checkAllMenuSubItemEventsNull(m)).getElement().getString();
			menuPanel.add(tb1, panel, true);
		}
	}
	
	private boolean checkAllMenuSubItemEventsNull(MenuItem item){
		if(item.getAssociatedEvent()!=null&&item.getSubItems().size()==0)
			return false;
		
		int overall = item.getSubItems().size();
		int nullCount = 0;
		for(MenuSubItem m : item.getSubItems()){
			if(m.getAssociatedEvent()==null)
				nullCount++;
		}
		if(nullCount!=overall)
			return false;
		else
			return true;
	}

	private HorizontalPanel getHeaderString(String text, ImageResource image, boolean crossed) {
		// Add the image and text to a horizontal panel
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(0);
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanel.add(new Image(image));
		HTML headerText = new HTML(text);
		if(!crossed)
			headerText.setStyleName("cw-StackPanelHeader");
		else
			headerText.setStyleName("crossedStackPanelHeader");
		hPanel.add(headerText);

		// Return the HTML string for the panel
		return hPanel;
	}


	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void setMenuItems(List<de.rowbuddy.client.menu.MenuItem> menuItems) {
		this.menuItems = menuItems;
		renderMenuItems();
	}

	@Override
	public List<MenuItem> getMenu() {
		return menuItems;
	}
}
