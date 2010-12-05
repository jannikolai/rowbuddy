package de.rowbuddy.client.presenter;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import de.rowbuddy.boundary.dtos.PersonalTripDTO;
import de.rowbuddy.client.events.AddBoatEvent;
import de.rowbuddy.client.events.BoatDetailEvent;
import de.rowbuddy.client.events.StartTripEvent;
import de.rowbuddy.client.services.LogbookRemoteServiceAsync;

public class ListPersonalTripsPresenter implements Presenter {

	public interface Display {
		Widget asWidget();

		HasClickHandlers getStartTripButton();

		HasClickHandlers getLogRowedTripButton();

		HasClickHandlers getTable();

		void setData(Collection<PersonalTripDTO> trips);

		int getClickedRow(ClickEvent event);
	}

	private final Display view;
	private final LogbookRemoteServiceAsync logbook;
	private final SimpleEventBus eventBus;
	private List<PersonalTripDTO> fetchedTrips;
	private static Logger logger = Logger
			.getLogger(ListPersonalTripsPresenter.class.getName());

	public ListPersonalTripsPresenter(LogbookRemoteServiceAsync logbook,
			Display view, SimpleEventBus eventBus) {
		this.logbook = logbook;
		this.view = view;
		this.eventBus = eventBus;
	}

	private void bind() {
		view.getStartTripButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				eventBus.fireEvent(new StartTripEvent());
			}
		});

		view.getLogRowedTripButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				// TODO: Implement Event
				// eventBus.fireEvent(new LogRowedTripEvent());
			}
		});

		view.getTable().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int selectedRow = view.getClickedRow(event);
				logger.info("Row " + selectedRow + " selected");
				if (selectedRow > 0) {
					Long id = fetchedTrips.get(selectedRow - 1).getId();

					logger.info("Fire TripDetailEvent id:" + id);
					// TODO: Implement
					// eventBus.fireEvent(new TripDetailEvent(id));
				}
			}
		});
	}

	@Override
	public void start(HasWidgets container) {
		bind();
		container.clear();
		container.add(view.asWidget());
		fetchPersonalTrips();
	}

	private void fetchPersonalTrips() {
		logbook.getPersonalTrips(new AsyncCallback<List<PersonalTripDTO>>() {

			@Override
			public void onSuccess(List<PersonalTripDTO> arg0) {
				view.setData(arg0);
			}

			@Override
			public void onFailure(Throwable arg0) {
				Window.alert(arg0.toString());
			}
		});
	}

}