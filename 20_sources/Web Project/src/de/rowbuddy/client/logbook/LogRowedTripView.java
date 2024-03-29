package de.rowbuddy.client.logbook;

import java.util.Date;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

import de.rowbuddy.client.HeaderButtonView;

public class LogRowedTripView extends HeaderButtonView implements
		LogRowedTripPresenter.Display {

	private FlexTable content;
	private TextBox routeName;
	private TextBox memberName;
	private TextBox boatName;
	private Button addButton;
	private Button cancelButton;
	private Button deleteTripMemberButton;
	private Button setCoxButton;
	private SuggestBox route;
	private SuggestBox member;
	private SuggestBox boat;
	private DateBox startDate;
	private DateBox endDate;
	private Date startTime;
	private Date endTime;
	private TextBox startEdit;
	private TextBox endEdit;
	private final ListBox multiBox;
	private DateTimeFormat timeDf = DateTimeFormat.getFormat("HH:mm");

	public LogRowedTripView(String pageTitle) {
		super(pageTitle);
		startTime = new Date(System.currentTimeMillis());
		endTime = new Date(System.currentTimeMillis() + 10000);

		content = new FlexTable();
		routeName = new TextBox();
		boatName = new TextBox();
		memberName = new TextBox();
		routeName = new TextBox();
		addButton = new Button("Hinzufügen");
		addButton.setStylePrimaryName("buttonApply buttonPositive");
		cancelButton = new Button("Abbrechen");
		cancelButton.setStylePrimaryName("buttonCancel buttonNegative");

		addButton(addButton);
		addButton(cancelButton);

		content.setText(0, 0, "Routenname:");
		content.setWidget(0, 1, routeName);

		content.setText(1, 0, "Boot:");
		content.setWidget(1, 1, boatName);

		content.setText(2, 0, "Mitglied(er):");
		content.setWidget(2, 1, memberName);

		multiBox = new ListBox(true);
		multiBox.ensureDebugId("cwListBox-multiBox");
		multiBox.setWidth("100%");
		multiBox.setVisibleItemCount(10);

		VerticalPanel multiBoxPanel = new VerticalPanel();
		multiBoxPanel.setSpacing(4);
		multiBoxPanel.add(multiBox);
		multiBoxPanel.setWidth("100%");
		content.setWidget(3, 1, multiBoxPanel);
		
		FlexTable ft2 = new FlexTable();
		
		deleteTripMemberButton = new Button("TripMember löschen");
		deleteTripMemberButton.setStylePrimaryName("buttonCancel buttonNegative");		
		ft2.setWidget(0, 0, deleteTripMemberButton);
		
		setCoxButton = new Button("Als Steuermann setzen");
		setCoxButton.setStylePrimaryName("buttonApply buttonRegular");		
		ft2.setWidget(1,0, setCoxButton);
		
		content.setWidget(3, 2, ft2);
		content.getFlexCellFormatter().setVerticalAlignment(3, 2, HasVerticalAlignment.ALIGN_TOP);
		
		DateTimeFormat dateFormat = DateTimeFormat
				.getFormat(PredefinedFormat.DATE_MEDIUM);

		startDate = new DateBox();
		startDate.setValue(new Date(System.currentTimeMillis()));
		startDate.setFormat(new DateBox.DefaultFormat(dateFormat));
		startDate.getDatePicker().addValueChangeHandler(
				new ValueChangeHandler<Date>() {

					@Override
					public void onValueChange(ValueChangeEvent<Date> arg0) {
						endDate.setValue(arg0.getValue());
					}
				});

		endDate = new DateBox();
		endDate.setValue(new Date(System.currentTimeMillis()));
		endDate.setFormat(new DateBox.DefaultFormat(dateFormat));

		content.setHTML(5, 0, "Startzeit:");
		HorizontalPanel startPanel = new HorizontalPanel();
		content.setWidget(5, 1, startPanel);
		startPanel.add(startDate);

		startEdit = new TextBox();
		startEdit.setText(timeDf.format(startTime));
		startPanel.add(startEdit);

		HorizontalPanel endPanel = new HorizontalPanel();

		content.setHTML(7, 0, "Endzeit:");

		content.setWidget(7, 1, endPanel);

		endEdit = new TextBox();
		endEdit.setText(timeDf.format(endTime));
		endPanel.add(endDate);
		endPanel.add(endEdit);
		setContent(content);
	}

	public void setBoatInformation(String name, boolean coxed, int rowers) {
		if (coxed) {
			content.setText(1, 2, "Gesteuert: ja");
		} else {
			content.setText(1, 2, "Gesteuert: nein");
		}
		content.setText(2, 2, "Bootsplätze: " + rowers);
	}

	public void setRouteInformation(double length) {
		content.setText(0, 2, "Streckenlänge: " + length + " km");
	}

	@Override
	public void showTripMembers(String[] tripMembers) {
		multiBox.clear();
		for (String tm : tripMembers) {
			multiBox.addItem(tm);
		}
	}

	@Override
	public HasClickHandlers getAddButton() {
		return addButton;
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return cancelButton;
	}

	@Override
	public HasValue<String> getRouteName() {
		return route;
	}

	@Override
	public HasValue<String> getBoatName() {
		return boat;
	}

	@Override
	public HasValue<String> getMemberName() {
		return member;
	}

	@Override
	public SuggestBox getMember() {
		return member;
	}

	@Override
	public ListBox getListBox() {
		return multiBox;
	}

	@Override
	public void setRouteOracle(SuggestOracle oracle) {
		route = new SuggestBox(oracle, routeName);
		content.setWidget(0, 1, route);
	}

	@Override
	public void setBoatOracle(SuggestOracle oracle) {
		boat = new SuggestBox(oracle, boatName);
		content.setWidget(1, 1, boat);
	}

	@Override
	public void setMemberOracle(SuggestOracle oracle) {
		member = new SuggestBox(oracle, memberName);
		content.setWidget(2, 1, member);
	}

	@Override
	public HasClickHandlers getDeleteTripMemberButton() {
		return deleteTripMemberButton;
	}

	@Override
	public HasClickHandlers getSetCoxButton() {
		return setCoxButton;
	}

	@Override
	public Date getStartDate() {
		startTime = timeDf.parse(startEdit.getValue());
		return mergeDateTime(startDate.getValue(), startTime);
	}

	@Override
	public Date getEndDate() {
		endTime = timeDf.parse(endEdit.getText());
		return mergeDateTime(endDate.getValue(), endTime);
	}

	private Date mergeDateTime(Date date, Date time) {
		DateTimeFormat dateDf = DateTimeFormat.getFormat("dd.MM.yyyy");
		DateTimeFormat mergedDf = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm");
		return mergedDf.parse(dateDf.format(date) + " " + timeDf.format(time));
	}
}
