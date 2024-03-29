package de.rowbuddy.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class GWTEntryPoint implements EntryPoint {

	private SimpleEventBus eventBus;

	private static Logger logger = Logger.getLogger(GWTEntryPoint.class
			.getName());

	public void onModuleLoad() {
		Maps.loadMapsApi("", "2", false, new Runnable() {
			public void run() {
				ServiceHolderFactory.fetchSessionMember(new Runnable() {

					@Override
					public void run() {
						load();
					}
				});
			}
		});
	}

	public void load() {
		HasWidgets mainPanel = initialMainPanel();
		FlowPanel messagePanel = new FlowPanel();
		eventBus = new SimpleEventBus();

		VerticalPanel vPanel = new VerticalPanel();

		RootPanel.get("Main").add(
				initalRootFlexTable(mainPanel, messagePanel, vPanel));

		AppController controller = new AppController(eventBus, messagePanel,
				vPanel, ServiceHolderFactory.getSessionMember());

		controller.start(mainPanel);
		logger.info("Application started");
	}

	private Widget initalRootFlexTable(HasWidgets mainPanel,
			Widget messagePanel, VerticalPanel vPanel) {

		final FlexTable flexTable = new FlexTable();
		FlexCellFormatter cellFormatter = flexTable.getFlexCellFormatter();
		flexTable.addStyleName("flexTable");
		flexTable.setWidth("80%");
		flexTable.setCellSpacing(5);
		flexTable.setCellPadding(3);

		cellFormatter.setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT);
		cellFormatter.setColSpan(0, 0, 2);
		cellFormatter
				.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
		cellFormatter.setRowSpan(1, 0, 2);
		cellFormatter.setHorizontalAlignment(1, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		cellFormatter.setHorizontalAlignment(2, 0,
				HasHorizontalAlignment.ALIGN_LEFT);

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setWidth("100%");
		hPanel.setStylePrimaryName("logoHeader");

		Label loginLabel = new Label("Logged in: "
				+ ServiceHolderFactory.getSessionMember().getEmail());

		Button logoutButton = new Button("Logout");
		logoutButton.setStylePrimaryName("buttonExit buttonNegative");
		logoutButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				ServiceHolderFactory.getSessionManager().logout(
						new ServerRequestHandler<Void>(eventBus, "Logout", null,
								null) {
							@Override
							public void onSuccess(Void arg0) {
								Window.Location.assign(GWT.getHostPageBaseURL()
										+ "Login.jsp");
							}
						});
			}
		});

		// HorizontalPanel verticalPanel = new HorizontalPanel();
		// verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		// verticalPanel.add(loginLabel);
		// verticalPanel.add(logoutButton);

		// hPanel.add(verticalPanel);

		// hPanel.setCellWidth(verticalPanel, "20%");

		hPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.setStylePrimaryName("loginLogout");
		horizontalPanel.add(loginLabel);
		horizontalPanel.add(logoutButton);
		hPanel.add(horizontalPanel);

		flexTable.setWidget(0, 0, hPanel);

		cellFormatter.setWidth(1, 0, "20%");

		flexTable.setWidget(1, 0, vPanel);

		// messagePanel.setStyleName("messages");
		// DecoratorPanel panel = new DecoratorPanel();
		//
		// panel.setWidget(messagePanel);
		flexTable.setWidget(1, 1, messagePanel);
		// panel.setStylePrimaryName("messages");
		flexTable.getCellFormatter().setStylePrimaryName(1, 0, "menuPanel");
		flexTable.getCellFormatter().setStyleName(1, 1, "messages");

		flexTable.setWidget(2, 0, (Widget) mainPanel);

		flexTable.getCellFormatter().setStylePrimaryName(2, 0, "viewPanel");

		return flexTable;
	}

	private HasWidgets initialMainPanel() {
		FlowPanel panel = new FlowPanel();

		return panel;
	}

}
