package de.rowbuddy.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

import de.rowbuddy.client.presenter.BoatPresenter;
import de.rowbuddy.client.presenter.Presenter;
import de.rowbuddy.client.services.BoatRemoteServiceAsync;
import de.rowbuddy.client.views.boat.BoatView;

public class AppController implements Presenter, ValueChangeHandler<String>{
	private SimpleEventBus eventBus;
	private BoatRemoteServiceAsync boatService;
	private HasWidgets container;
	public AppController(BoatRemoteServiceAsync boatService, SimpleEventBus eventBus) {
		this.boatService = boatService;
	}  
	
	//bind Event handling here
	private void bind(){
		History.addValueChangeHandler(this);
	}
	
	@Override
	public void onValueChange(ValueChangeEvent<String> arg0) {
	//	arg0.get
		String token = arg0.getValue();
		if(token != null) {
			Presenter presenter = null;
			if(token.equals("listBoats")) {
				presenter = new BoatPresenter(boatService, new BoatView(), eventBus);
			} 

			if(presenter != null) {
				presenter.start(container);
			}
		}	
	}

	@Override
	public void start(HasWidgets container) {
		// TODO Auto-generated method stub
		bind();
		this.container = container;
		if(History.getToken().equals("")) {
			History.newItem("listBoats");
		} else {
			History.fireCurrentHistoryState();
		}
	}

}