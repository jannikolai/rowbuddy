package de.rowbuddy.client;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

import de.rowbuddy.client.events.AbstractEvent;
import de.rowbuddy.client.events.PresenterChanger;
import de.rowbuddy.client.events.PresenterChanger.EventListener;
import de.rowbuddy.client.events.AddBoatPresenterChanger;
import de.rowbuddy.client.events.AddBoatDamagePresenterChanger;
import de.rowbuddy.client.events.DetailsBoatPresenterChanger;
import de.rowbuddy.client.events.ListBoatsEvent;
import de.rowbuddy.client.events.ListBoatsPresenterChanger;
import de.rowbuddy.client.events.DetailsBoatDamagePresenterChanger;
import de.rowbuddy.client.events.EditBoatDamagePresenterChanger;
import de.rowbuddy.client.events.EditBoatPresenterChanger;
import de.rowbuddy.client.events.ListBoatDamagePresenterChanger;
import de.rowbuddy.client.events.ListPersonalTripsPresenterChanger;
import de.rowbuddy.client.presenter.MenuPresenter;
import de.rowbuddy.client.presenter.Presenter;
import de.rowbuddy.client.presenter.StatusMessagePresenter;
import de.rowbuddy.client.services.BoatRemoteServiceAsync;
import de.rowbuddy.client.services.LogbookRemoteServiceAsync;
import de.rowbuddy.client.views.MenuView;
import de.rowbuddy.client.views.MessageView;

public class AppController implements Presenter, EventListener {
	private SimpleEventBus eventBus;
	private BoatRemoteServiceAsync boatService;
	private LogbookRemoteServiceAsync logbookService;
	private HasWidgets container;
	private StatusMessagePresenter statusPresenter;
	private Presenter menuPresenter;
	private List<PresenterChanger> eventHandlers = new LinkedList<PresenterChanger>();

	public AppController(BoatRemoteServiceAsync boatService,
			LogbookRemoteServiceAsync logbookService, SimpleEventBus eventBus,
			HasWidgets messageContainer, HasWidgets menuPanel) {
		this.boatService = boatService;
		this.logbookService = logbookService;
		this.eventBus = eventBus;
		this.statusPresenter = new StatusMessagePresenter(new MessageView(),
				eventBus);
		statusPresenter.start(messageContainer);
		menuPresenter = new MenuPresenter(new MenuView(), eventBus);
		menuPresenter.start(menuPanel);
	}

	private void bindBoatEventHandlers() {
		eventHandlers
				.add(new ListBoatsPresenterChanger(eventBus, container, boatService));
		eventHandlers.add(new DetailsBoatPresenterChanger(eventBus, container,
				boatService));
		eventHandlers.add(new AddBoatPresenterChanger(container, eventBus,
				boatService));
		eventHandlers
				.add(new AddBoatDamagePresenterChanger(container, eventBus, boatService));
		eventHandlers.add(new DetailsBoatDamagePresenterChanger(container, eventBus,
				boatService));
		eventHandlers.add(new EditBoatDamagePresenterChanger(container, eventBus,
				boatService));

		eventHandlers.add(new EditBoatPresenterChanger(container, eventBus,
				boatService));

		eventHandlers.add(new ListBoatDamagePresenterChanger(container, eventBus,
				boatService));
	}

	private void bindLogbookEventHandlers() {

		eventHandlers.add(new ListPersonalTripsPresenterChanger(container,
				eventBus, logbookService));

	}

	@Override
	public void start(HasWidgets container) {
		this.container = container;

		bindBoatEventHandlers();
		bindLogbookEventHandlers();

		// register fot presenter changes
		for (PresenterChanger eventHandler : eventHandlers) {
			eventHandler.addObserver(this);
		}

		if (History.getToken().equals("")) {
			eventBus.fireEvent(new ListBoatsEvent());
			// TODO welcome page
		} else {
			History.fireCurrentHistoryState();
		}
	}

	@Override
	public void eventProcessed(AbstractEvent<?> event) {
		statusPresenter.clear();
	}
}
