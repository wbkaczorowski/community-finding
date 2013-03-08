package pl.edu.pw.elka.inz.socialalgorithms.app.controller.events;

public class Event {

	EventName eventName;

	public Event(EventName eventName) {
		this.eventName = eventName;
	}

	public Object getEventName() {
		return eventName;
	}

}
