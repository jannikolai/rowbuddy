package de.rowbuddy.client.events;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

import de.rowbuddy.client.FadeAnimation;
import de.rowbuddy.client.presenter.Presenter;

public abstract class AbstractEventHandler implements EventHandler,
		ValueChangeHandler<String> {

	public interface EventListener {
		void eventProcessed(AbstractEvent<?> event);
	}

	private final HasWidgets targetWidget;
	private final static Logger logger = Logger
			.getLogger(AbstractEventHandler.class.getName());
	private final List<EventListener> listeners = new LinkedList<EventListener>();
	protected final EventBus eventBus;

	public AbstractEventHandler(HasWidgets targetWidget, EventBus eventBus) {
		this.targetWidget = targetWidget;
		this.eventBus = eventBus;

		this.eventBus.addHandler(getType(), this);
		History.addValueChangeHandler(this);
	}

	public void processEvent(AbstractEvent<?> event) {
		History.newItem(event.toHistoryItem());
		Presenter presenter = createPresenter(event);
		for (EventListener listener : listeners) {
			listener.eventProcessed(event);
		}
		FadeAnimation fade = new FadeAnimation(targetWidget, presenter);
		fade.run(400);
	}

	public void processHistoryEntry(String historyEntry) {
		AbstractEvent<?> event = toEvent(historyEntry);
		Presenter presenter = createPresenter(event);
		for (EventListener listener : listeners) {
			listener.eventProcessed(event);
		}
		FadeAnimation fade = new FadeAnimation(targetWidget, presenter);
		fade.run(200);
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> arg0) {
		String historyToken = arg0.getValue();
		if (historyToken.startsWith(getHistoryIdentifier())) {
			logger.info("History event processed: " + historyToken);
			processHistoryEntry(historyToken);
		}
	}

	public void addObserver(EventListener observer) {
		listeners.add(observer);
	}

	public abstract AbstractEvent<?> toEvent(String historyItem);

	public abstract Presenter createPresenter(AbstractEvent<?> event);

	protected abstract <T extends AbstractEventHandler> Type<T> getType();

	protected abstract String getHistoryIdentifier();

}
