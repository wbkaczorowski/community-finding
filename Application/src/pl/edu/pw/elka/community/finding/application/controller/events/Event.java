package pl.edu.pw.elka.community.finding.application.controller.events;

public class Event {

	EventName eventName;

	public Event(EventName eventName) {
		this.eventName = eventName;
	}

	public Object getEventName() {
		return eventName;
	}

}
