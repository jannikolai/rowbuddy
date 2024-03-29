package de.rowbuddy.client.route;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MapRightClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import de.rowbuddy.client.ServerRequestHandler;
import de.rowbuddy.client.Presenter;
import de.rowbuddy.client.events.DetailsRouteEvent;
import de.rowbuddy.client.events.ListRoutesEvent;
import de.rowbuddy.client.events.StatusMessageEvent;
import de.rowbuddy.client.model.StatusMessage;
import de.rowbuddy.client.services.RouteRemoteServiceAsync;
import de.rowbuddy.entities.GpsPoint;
import de.rowbuddy.entities.Route;
import de.rowbuddy.exceptions.RowBuddyException;

public class EditRoutePresenter implements Presenter {

	public interface Display {
		HasClickHandlers getSubmitButton();

		HasClickHandlers getCancelButton();

		HasValue<String> getName();

		HasValue<String> getLength();

		HasValue<String> getDescription();

		HasValue<Boolean> isMutable();

		MapWidget getMap();

		void setMap(LatLng[] points, Polyline polyline);

		Widget asWidget();

		HasClickHandlers getPopUpButton();

		void showPopUp();

		HasClickHandlers getDeleteButton();

		void closeDialog();
	}

	private Display view;
	private RouteRemoteServiceAsync routeService;
	private EventBus eventBus;
	private Route route;
	private Long id;
	private Logger logger = Logger
			.getLogger(EditRoutePresenter.class.getName());
	private List<LatLng> points;
	private Polyline polyline;

	public EditRoutePresenter(Display view,
			RouteRemoteServiceAsync routeService, EventBus eventBus, Long id) {
		this.view = view;
		this.routeService = routeService;
		this.eventBus = eventBus;
		this.id = id;
		points = new LinkedList<LatLng>();
	}

	@Override
	public void start(HasWidgets container) {
		container.clear();
		bind();
		fetchRoute();
		container.add(view.asWidget());
	}

	private void fetchRoute() {

		logger.info("Get Route id:" + id);
		routeService.getRouteForEdit(id, new ServerRequestHandler<Route>(
				eventBus, "Route bearbeiten", null, new DetailsRouteEvent(id)) {

			public void onSuccess(Route arg0) {
				route = arg0;
				view.getName().setValue(route.getName());
				view.getLength().setValue("" + route.getLengthKM());
				view.getDescription().setValue(route.getShortDescription());
				view.isMutable().setValue(route.isMutable());
				logger.info("WayPoints: " + route.getWayPoints().size());

				if (!route.getWayPoints().isEmpty()) {
					LatLng[] points = new LatLng[route.getWayPoints().size()];
					int i = 0;
					for (GpsPoint point : route.getWayPoints()) {
						points[i] = LatLng.newInstance(point.getLatitude(),
								point.getLongitude());
						EditRoutePresenter.this.points.add(points[i]);
						i++;
					}
					polyline = new Polyline(points);
					view.setMap(points, polyline);
				}
			};
		});
	}

	private boolean updateRoute() {
		boolean success = false;
		try {
			route.setName(view.getName().getValue());
			route.setLengthKM(Double.parseDouble(view.getLength().getValue()));
			route.setShortDescription(view.getDescription().getValue());
			route.setMutable(view.isMutable().getValue());
			List<GpsPoint> wayPoints = new LinkedList<GpsPoint>();
			for (LatLng latlng : points) {
				wayPoints.add(new GpsPoint(latlng.getLatitude(), latlng
						.getLongitude()));
			}
			route.setWayPoints(wayPoints);
			logger.info("Update route with id:" + route.getId());
			success = true;
		} catch (RowBuddyException ex) {
			logger.warning(ex.getMessage());
			StatusMessage message = new StatusMessage(false);
			message.setMessage(ex.getMessage());
			eventBus.fireEvent(new StatusMessageEvent(message));
		}
		return success;
	}

	private void bind() {
		view.getSubmitButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (updateRoute()) {
					routeService.editRoute(route,
							new ServerRequestHandler<Route>(eventBus,
									"Route speichern", new ListRoutesEvent(),
									null));
				}
			}
		});

		view.getCancelButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				eventBus.fireEvent(new ListRoutesEvent());
			}
		});

		view.getPopUpButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				view.showPopUp();
			}
		});

		view.getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				view.closeDialog();
				routeService
						.deleteRoute(id, new ServerRequestHandler<Void>(eventBus,
								"Route löschen", new ListRoutesEvent(), null));
			}
		});

		view.getMap().addMapClickHandler(new MapClickHandler() {

			@Override
			public void onClick(MapClickEvent event) {
				view.getMap().addOverlay(new Marker(event.getLatLng()));
				points.add(event.getLatLng());
				LatLng[] latLngs = new LatLng[points.size()];
				if (polyline != null) {
					view.getMap().removeOverlay(polyline);
				}
				polyline = new Polyline(points.toArray(latLngs));
				view.getMap().addOverlay(polyline);
				view.getLength().setValue("" + calcDistance());
				logger.info("Marker add: " + points.size());
			}
		});

		view.getMap().addMapRightClickHandler(new MapRightClickHandler() {

			@Override
			public void onRightClick(MapRightClickEvent event) {
				if (event.getOverlay() instanceof Marker) {
					Marker marker = (Marker) event.getOverlay();
					view.getMap().removeOverlay(marker);
					points.remove(marker.getLatLng());
					view.getMap().removeOverlay(polyline);
					LatLng[] latLngs = new LatLng[points.size()];
					polyline = new Polyline(points.toArray(latLngs));
					view.getMap().addOverlay(polyline);
					view.getLength().setValue("" + calcDistance());
					logger.info("Marker remove: " + points.size());
				}
			}
		});
	}

	private double calcDistance() {
		double lengthInMeter = 0.0;
		if (points.size() > 1) {
			Iterator<LatLng> iterator = points.iterator();
			LatLng point = iterator.next();
			while (iterator.hasNext()) {
				LatLng pointNext = iterator.next();
				lengthInMeter += point.distanceFrom(pointNext);
				point = pointNext;
			}
		}
		return Math.round((lengthInMeter / 1000.0) * 100.) / 100.;
	}

}
