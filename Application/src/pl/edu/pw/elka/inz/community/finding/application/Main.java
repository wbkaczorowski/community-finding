package pl.edu.pw.elka.inz.community.finding.application;

import pl.edu.pw.elka.inz.community.finding.application.controller.Controller;
import pl.edu.pw.elka.inz.community.finding.application.controller.events.EventsBlockingQueue;
import pl.edu.pw.elka.inz.community.finding.application.model.Model;
import pl.edu.pw.elka.inz.community.finding.application.view.View;

public class Main {

	public static void main(String[] args) {

		EventsBlockingQueue blockingQueue = new EventsBlockingQueue();
		Model model = new Model(blockingQueue);
		View view = new View(blockingQueue);
		Controller controller = new Controller(model, view, blockingQueue);

		controller.programStart();
	}

}
