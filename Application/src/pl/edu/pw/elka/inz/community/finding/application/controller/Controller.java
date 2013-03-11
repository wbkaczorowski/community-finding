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
			//put the starting event
			blockingQueue.put(new Event(EventName.START));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Loading handlers for servicing events.
	 */
	private void loadHandlers() {
		eventHandlers.put(EventName.START, new EventHandler() {

			@Override
			public void execute() {
				view.getControlPanel().setAlgorithmType(model.getAlgorithmManager().getAlgorithmType());
				view.showWindow();
			}
		});

		eventHandlers.put(EventName.OPEN_FILE, new EventHandler() {

			@Override
			public void execute() {
				if (view.getGraphFilePath() != null) {
					view.getStatusBar().setAppState("loading graph...");
					model.loadNewGraph(view.getGraphFilePath());
					view.setGraphView(model.getGraph());
					view.getStatusBar().setAppState("loaded: " + view.getGraphFilePath());
				}
			}
		});
		
		eventHandlers.put(EventName.CALCULATE, new EventHandler() {
			
			@Override
			public void execute() {
				model.setAlgorithmType(view.getControlPanel().getAlgorithmType());
				model.compute();
				view.newGroups(model.getGraph());
				
			}
		});
	}

	/**
	 * Main loop of application, handles event queue.
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
