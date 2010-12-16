package de.rowbuddy.client.views.member;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.rowbuddy.client.PageTitles;
import de.rowbuddy.client.presenter.member.ImportMembersPresenter;
import de.rowbuddy.client.views.HeaderButtonView;

public class ImportMembersView extends HeaderButtonView implements
		ImportMembersPresenter.Display {

	private Button startImportButton;
	private TextArea textArea;

	public ImportMembersView() {
		super(PageTitles.MEMBER_IMPORT);

		startImportButton = new Button("Import starten");
		startImportButton.setStyleName("buttonAdd buttonRegular");
		addButton(startImportButton);

		StringBuilder sb = new StringBuilder();
		sb.append("F�gen Sie die zu importierenden Daten in das Textfeld ein und klicken Sie auf den Button 'Import starten'. ");
		sb.append("Die Daten m�ssen kommagetrennt sein und mit der Zeile 'ID,Nachname,Name,Adresse,PLZ,Stadt,Telefon,Handy,Email' anfangen");

		Label label = new Label();
		label.setText(sb.toString());

		textArea = new TextArea();
		textArea.setVisibleLines(15);

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.add(label);
		vPanel.add(textArea);

		setContent(vPanel);
	}

	@Override
	public HasClickHandlers getImportButton() {
		return startImportButton;
	}

	@Override
	public String getImportData() {
		return textArea.getText();
	}

}