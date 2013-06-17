package pl.edu.pw.elka.community.finding.application.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pl.edu.pw.elka.community.finding.application.controller.events.EventsBlockingQueue;
import pl.edu.pw.elka.community.finding.application.model.algoritms.AlgorithmManager;
import pl.edu.pw.elka.community.finding.application.model.algoritms.AlgorithmType;
import pl.edu.pw.elka.community.finding.application.model.generators.RandomGraphGenerator;
import pl.edu.pw.elka.community.finding.application.model.generators.RandomGraphType;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Node;
import pl.edu.pw.elka.community.finding.application.model.tests.TestManager;
import edu.uci.ics.jung.graph.Graph;


/**
 * Main class for model in MVC pattern. 
 * @author Wojciech Kaczorowski
 *
 */
public class Model {

	private EventsBlockingQueue blockingQueue;
	private Graph<Node, Edge> graph = null;
	private AlgorithmManager algorithmManager;
	private RandomGraphGenerator randomGraphGenerator;
	private TestManager testManager;
	
	public Model(EventsBlockingQueue blockingQueue) {
		this.blockingQueue = blockingQueue;
		this.algorithmManager = new AlgorithmManager(blockingQueue);
		this.randomGraphGenerator = new RandomGraphGenerator();
		this.testManager = new TestManager(algorithmManager, randomGraphGenerator);
	}

	public void loadNewGraph(String filename) {
		try {
			graph = GraphUtils.read(filename);
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

	public Map<String, Graph<Node, Edge>> loadGraphs(String dirGraphPath) {
		Map<String, Graph<Node, Edge>> map = new HashMap<>();
		File directory = new File(dirGraphPath);
		for(File graphFile : directory.listFiles()) {
			if (graphFile.isFile() && (graphFile.getName().endsWith(".graphml") || graphFile.getName().endsWith(".paj"))) {
				try {
					map.put(graphFile.getName(), GraphUtils.read(graphFile.getAbsolutePath()));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	
	public TestManager getTestManager() {
		return testManager;
	}
	
	public void generateGraph(RandomGraphType randomGraphType, Properties properties) {
		graph = randomGraphGenerator.create(randomGraphType, properties);
	}
}
