package pl.edu.pw.elka.community.finding.application.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pl.edu.pw.elka.community.finding.application.controller.events.EventsBlockingQueue;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.graph.Graph;


/**
 * Main class for model in MVC pattern. 
 * @author Wojciech Kaczorowski
 *
 */
public class Model {

	private EventsBlockingQueue blockingQueue;
	private GraphReader graphReader;
	private Graph<Node, Edge> graph = null;
	private AlgorithmManager algorithmManager;

	public Model(EventsBlockingQueue blockingQueue) {
		this.blockingQueue = blockingQueue;
		this.algorithmManager = new AlgorithmManager(blockingQueue);
	}

	public void loadNewGraph(String filename) {
		try {
			this.graphReader = new GraphReader(filename);
			graph = graphReader.getGraph();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Graph<Node, Edge> getGraph() {
		return graph;
	}
	
	public AlgorithmManager getAlgorithmManager() {
		return algorithmManager;
	}

	public void setAlgorithmType(AlgorithmType algorithmType) {
		algorithmManager.setAlgorithmType(algorithmType);
	}

	public void loadGraphs(String dirGraphPath) {
		List<Graph<Node, Edge>> graphList = new ArrayList<Graph<Node, Edge>>();
		// TODO
	}

}
