package de.rowbuddy.client.boat;

import java.util.Collection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import de.rowbuddy.boundary.dtos.BoatDTO;
import de.rowbuddy.client.HeaderButtonView;
import de.rowbuddy.client.PageTitles;

public class ListBoatsView extends HeaderButtonView implements ListBoatsPresenter.Display {

	private Button addButton;
	private FlexTable boatTable;

	public ListBoatsView() {
		super(PageTitles.BOAT_OVERVIEW);

		addButton = new Button("Boot Hinzufügen");
		addButton.setStyleName("buttonAdd buttonRegular");
		addButton(addButton);

		boatTable = new FlexTable();
		boatTable.setWidth("100%");
		boatTable.setStyleName("boatTable");
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.add(boatTable);
		scrollPanel.setStyleName("scollTable");
		setContent(scrollPanel);
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public HasClickHandlers getAddButton() {
		return addButton;
	}

	@Override
	public int getClickedRow(ClickEvent event) {
		int selectedRow = -1;
		HTMLTable.Cell cell = boatTable.getCellForEvent(event);

		if (cell != null) {
			selectedRow = cell.getRowIndex();
		}
		return selectedRow;
	}

	public void initTableHead() {
		boatTable.setText(0, 0, "Name");
		boatTable.setText(0, 1, "Bootsplätze");
		boatTable.setText(0, 2, "Gesteuert");
		boatTable.setText(0, 3, "Gesperrt");
		boatTable.getRowFormatter().setStyleName(0, "boatTableHeader");
		boatTable.setCellPadding(5);

	}

	@Override
	public void setData(Collection<BoatDTO> boats) {
		boatTable.removeAllRows();
		initTableHead();
		int i = 1;
		for (BoatDTO boat : boats) {
			boatTable.setText(i, 0, boat.getName());
			boatTable.setText(i, 1, String.valueOf(boat.getNumberOfSeats()));

			CheckBox coxBox = new CheckBox();
			coxBox.setEnabled(false);
			coxBox.setValue(boat.isCoxed());
			boatTable.setWidget(i, 2, coxBox);

			CheckBox lockedBox = new CheckBox();
			lockedBox.setEnabled(false);
			lockedBox.setValue(boat.isLocked());
			boatTable.setWidget(i, 3, lockedBox);

			HTMLTable.RowFormatter rf = boatTable.getRowFormatter();

			if ((i % 2) != 0) {
				rf.setStylePrimaryName(i, "FlexTable-OddRow");
			} else {
				rf.addStyleName(i, "FlexTable-EvenRow");
			}
			i++;
		}
	}

	@Override
	public HasClickHandlers getTable() {
		return boatTable;
	}

	@Override
	public UIObject getUiAddButton() {
		return addButton;
	}

}
