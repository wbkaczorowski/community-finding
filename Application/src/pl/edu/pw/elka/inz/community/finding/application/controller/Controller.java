package pl.edu.pw.elka.inz.community.finding.application.controller;

import javax.swing.Timer;

import pl.edu.pw.elka.inz.community.finding.application.controller.events.Event;
import pl.edu.pw.elka.inz.community.finding.application.controller.events.EventHandler;
import pl.edu.pw.elka.inz.community.finding.application.controller.events.EventName;
import pl.edu.pw.elka.inz.community.finding.application.controller.events.EventsBlockingQueue;
import pl.edu.pw.elka.inz.community.finding.application.controller.events.EventsHandlersMap;
import pl.edu.pw.elka.inz.community.finding.application.model.Model;
import pl.edu.pw.elka.inz.community.finding.application.view.View;

public class Controller {

	EventsBlockingQueue blockingQueue;
	Model model;
	View view;
	EventsHandlersMap eventHandlers = new EventsHandlersMap();
	Timer timer;

	public Controller(Model model, View view, EventsBlockingQueue blockingQueue) {
		this.model = model;
		this.view = view;
		this.blockingQueue = blockingQueue;

		this.loadHandlers();

		try {
			blockingQueue.put(new Event(EventName.SHOW_EMPTY_WINDOW));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Ładuje handlery do obsługi zdarzeń
	 */
	private void loadHandlers() {
		eventHandlers.put(EventName.SHOW_EMPTY_WINDOW, new EventHandler() {

			@Override
			public void execute() {
				view.showWindow();

			}
		});

		eventHandlers.put(EventName.CHOOSE_FILE, new EventHandler() {

			@Override
			public void execute() {
				model.loadNewGraph(view.getGraphFilePath());
				view.setGraphView(model.getGraph());
				view.getStatusBar().setAppState("Wczytano: " + view.getGraphFilePath());
			}
		});
		
		eventHandlers.put(EventName.CALCULATE, new EventHandler() {
			
			@Override
			public void execute() {
				model.compute();
//				view.newGroups(model.getGraph());
				
			}
		});
	}

	/**
	 * Główna pętla programu obsługująca kolejkę zdarzeń
	 */
	public void programStart() {

		Event event;
		EventHandler eventHandler;

		while (true) {
			try {
				event = this.blockingQueue.take();
				eventHandler = eventHandlers.get(event.getEventName());
				eventHandler.execute();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
