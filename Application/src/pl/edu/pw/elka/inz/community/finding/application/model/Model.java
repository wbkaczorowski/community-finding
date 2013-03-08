package pl.edu.pw.elka.inz.community.finding.application.model;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import cern.colt.matrix.impl.SparseDoubleMatrix1D;

import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.graph.Graph;

import pl.edu.pw.elka.inz.community.finding.application.controller.events.EventsBlockingQueue;
import pl.edu.pw.elka.inz.community.finding.application.model.algorithms.*;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Node;

public class Model {

	private EventsBlockingQueue blockingQueue;
	private InputGraph inputGraph;
	private Graph<Node, Edge> graph = null;
	private Algorithm algorithm;

	public Model(EventsBlockingQueue blockingQueue) {
		this.blockingQueue = blockingQueue;
	}

	public void loadNewGraph(String filename) {
		try {
			this.inputGraph = new InputGraph(filename);
			graph = inputGraph.getGraph();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void compute() {

		//TODO tutaj wybierałka zeby w zaleznosci jakim chcemy obliczalo
//		algorithm = new GirvanNewman();
//		algorithm = new Bicomponent();
//		algorithm = new Voltage();
//		algorithm = new WeakComponent();
		algorithm = new Louvain();
		algorithm.setGraph(graph);
		long time = System.currentTimeMillis();
		algorithm.calculate();
		long timeTotal = System.currentTimeMillis() - time;
		System.out.println("Czas obliczen: " + timeTotal + " ms");
		
		
		
		
	}
	
	public Graph<Node, Edge> getGraph() {
		return graph;
	}

}
