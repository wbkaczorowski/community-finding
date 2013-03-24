package pl.edu.pw.elka.community.finding.application.model;

import java.util.Set;

import pl.edu.pw.elka.community.finding.algorithms.Louvain;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.graph.Graph;

/**
 * Manager for community finding algorithms. Manages tests, chose correct parameters etc.
 * 
 * @author Wojciech Kaczorowski
 *
 */
public class AlgorithmManager {

	/**
	 * Type of algorithm actually use.
	 */
	private AlgorithmType algorithmType;
		
	public AlgorithmManager() {
		// default algorithm
		this.algorithmType = AlgorithmType.GRIVAN_NEWMAN;
	}
	
	public int computeSingle(Graph<Node, Edge> graph, int param) {
		int numberGroups;
		long time = System.currentTimeMillis();		

		switch (algorithmType) {
		case LOUVAIN:
			numberGroups = louvain(graph);
			break;
			
		case GRIVAN_NEWMAN:
			numberGroups = grivanNewman(graph, param);
			break;
			
		case WU_HUBERMAN:
			numberGroups = wuHuberman();
			break;
			
		case CLAUSET_NEWMAN_MOORE:
			numberGroups = clausetNewmanMoore();
			break;
			
		default:
			numberGroups = 0;
			break;
		}
				
		long timeTotal = System.currentTimeMillis() - time;
		System.out.println("Czas obliczen: " + timeTotal + " ms");
		return numberGroups;
	}	
	
	private int louvain(Graph<Node, Edge> graph) {
		Louvain<Node, Edge> louvain = new Louvain<Node, Edge>();
		louvain.setGraph(graph);
		return louvain.getCommunities().size();
		
	}
	
	private int grivanNewman(Graph<Node, Edge> graph, int numEdgesToRemove) {
		EdgeBetweennessClusterer<Node, Edge> algorithm = new EdgeBetweennessClusterer<Node, Edge>(numEdgesToRemove);
		int groupCounter = 0;
		for (Set<Node> set : algorithm.transform(graph)) {
			for (Node n : set) {
				n.setGroup(String.valueOf(groupCounter));
			}
			++groupCounter;
		}
		return groupCounter;
	}
	
	private int wuHuberman() {
		return 0;
		
	}
	
	private int clausetNewmanMoore() {
		return 0;
		
	}
	
	public AlgorithmType getAlgorithmType() {
		return algorithmType;
	}

	public void setAlgorithmType(AlgorithmType algorithmType) {
		this.algorithmType = algorithmType;
	}
	
}
