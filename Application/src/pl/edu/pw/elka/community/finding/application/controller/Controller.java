package pl.edu.pw.elka.community.finding.application.controller;

import pl.edu.pw.elka.community.finding.application.controller.events.Event;
import pl.edu.pw.elka.community.finding.application.controller.events.EventHandler;
import pl.edu.pw.elka.community.finding.application.controller.events.EventName;
import pl.edu.pw.elka.community.finding.application.controller.events.EventsBlockingQueue;
import pl.edu.pw.elka.community.finding.application.controller.events.EventsHandlersMap;
import pl.edu.pw.elka.community.finding.application.model.AlgorithmType;
import pl.edu.pw.elka.community.finding.application.model.Model;
import pl.edu.pw.elka.community.finding.application.view.View;

public class Controller {

	EventsBlockingQueue blockingQueue;
	Model model;
	View view;
	EventsHandlersMap eventHandlers = new EventsHandlersMap();

	public Controller(Model model, View view, EventsBlockingQueue blockingQueue) {
		this.model = model;
		this.view = view;
		this.blockingQueue = blockingQueue;

		this.loadHandlers();

		try {
			// put the starting event
//			blockingQueue.put(new Event(EventName.START));
			blockingQueue.put(new Event(EventName.DEBUG));
		} catch (InterruptedException e) {
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
					view.setGraphParameter(model.getGraph());
					view.getStatusBar().setAppState("loaded: " + view.getGraphFilePath());
				}
			}
		});
		
		eventHandlers.put(EventName.REFRESH_VIEW, new EventHandler() {
			@Override
			public void execute() {
				view.setGraphView(model.getGraph());
				view.setGraphParameter(model.getGraph());
			}
		});

		eventHandlers.put(EventName.OPEN_DIRECTORY, new EventHandler() {

			@Override
			public void execute() {
				if (view.getDirGraphPath() != null) {
					view.getStatusBar().setAppState("loading graphs...");
					model.loadGraphs(view.getDirGraphPath());
					// no putting graph view here
					view.getStatusBar().setAppState("loaded graphs: " + view.getDirGraphPath());
				}
			}
		});

		eventHandlers.put(EventName.CALCULATE_SINGLE, new EventHandler() {

			@Override
			public void execute() {
				model.setAlgorithmType(view.getControlPanel().getSingleTestWindow().getChosenAglorithm());
				view.getStatusBar().setAppState("calculating...");
				view.getStatusBar().setAppState("number of groups: " + model.getAlgorithmManager().computeSingle(model.getGraph(), view.getControlPanel().getSingleTestWindow().getParam()));
				view.newGroups(model.getGraph());
			}
		});
		
		eventHandlers.put(EventName.DEBUG, new EventHandler() {
			
			@Override
			public void execute() {
				view.showWindow();
				
				view.getStatusBar().setAppState("loading graph...");
				model.loadNewGraph("sm.graphml");
				view.setGraphView(model.getGraph());
				view.setGraphParameter(model.getGraph());
				view.getStatusBar().setAppState("loaded: " + view.getGraphFilePath());
				model.setAlgorithmType(AlgorithmType.FAST_NEWMAM);
				view.getStatusBar().setAppState("calculating...");
				view.getStatusBar().setAppState("number of groups: " + model.getAlgorithmManager().computeSingle(model.getGraph(), 0));
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
