package de.rowbuddy.client.presenter;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import de.rowbuddy.client.services.BoatRemoteServiceAsync;

public class ListDamagePresenter implements Presenter{
	
	public interface Display{
		HasValue<Boolean> getHistory();
		void addDamageRow(String name, String reporter, String date, boolean isOpen);
		Widget asWidget();
	}
	
	private Display view;
	private BoatRemoteServiceAsync service;
	private SimpleEventBus eventBus;
	
	public ListDamagePresenter(Display view, BoatRemoteServiceAsync service, SimpleEventBus eventBus){
		this.view = view;
		this.service = service;
		this.eventBus = eventBus;
	}
	
	@Override
	public void start(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

}