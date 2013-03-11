package pl.edu.pw.elka.inz.community.finding.application.model;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pl.edu.pw.elka.inz.community.finding.application.controller.events.EventsBlockingQueue;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.graph.Graph;


/**
 * Main class for model in MVC pattern. 
 * @author Wojciech Kaczorowski
 *
 */
public class Model {

	private EventsBlockingQueue blockingQueue;
	private InputGraph inputGraph;
	private Graph<Node, Edge> graph = null;
	private AlgorithmManager algorithmManager;

	public Model(EventsBlockingQueue blockingQueue) {
		this.blockingQueue = blockingQueue;
		this.algorithmManager = new AlgorithmManager();
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

}
