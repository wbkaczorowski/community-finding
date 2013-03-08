package pl.edu.pw.elka.inz.socialalgorithms.app;

import pl.edu.pw.elka.inz.socialalgorithms.app.controller.Controller;
import pl.edu.pw.elka.inz.socialalgorithms.app.controller.events.EventsBlockingQueue;
import pl.edu.pw.elka.inz.socialalgorithms.app.model.Model;
import pl.edu.pw.elka.inz.socialalgorithms.app.view.View;

public class Main {

    public static void main(String[] args) {

	EventsBlockingQueue blockingQueue = new EventsBlockingQueue();
	Model model = new Model(blockingQueue);
	View view = new View(blockingQueue);
	Controller controller = new Controller(model, view, blockingQueue);

	controller.programStart();
    }

}
