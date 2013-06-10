package pl.edu.pw.elka.community.finding.application.model.generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

import pl.edu.pw.elka.community.finding.application.model.graph.EdgeFactory;
import pl.edu.pw.elka.community.finding.application.model.graph.GraphFactory;
import pl.edu.pw.elka.community.finding.application.model.graph.NodeFactory;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

/**
 * Generator of artificial graphs.
 * @author Wojciech Kaczorowski
 *
 */
public class RandomGraphGenerator {
	
	private Random random;
	
	public RandomGraphGenerator() {
		random = new Random();
	}
	
	
	private Graph generateRandomGraph(int nodes, int edges){
		
		Graph<Node, Edge> graph = new UndirectedSparseGraph<>();
		for (int n = 0; n < nodes; ++n) {
			Properties properties = new Properties();
			properties.put("id", String.valueOf(n));
			Node node = new Node(properties);
			node.setGroup("0");
			graph.addVertex(node);
		}
		ArrayList<Node> vertices = new ArrayList<Node>(graph.getVertices());
		
		for (int e = 0; e < edges; ++e) {
			while (true) {
				int firstNode = random.nextInt(nodes);
				int secondNode = random.nextInt(nodes);
				if (firstNode != secondNode && graph.findEdge(vertices.get(firstNode), vertices.get(secondNode)) == null) {
					graph.addEdge(new Edge(e), vertices.get(firstNode), vertices.get(secondNode));
					break;
				}
			}
		}
		return graph;
	}
	

	private Graph generateErdosRenyi(int nodes, double probability) {
		ErdosRenyiGenerator<Node, Edge> erdosRenyiGenerator = new ErdosRenyiGenerator<>(new GraphFactory(), new NodeFactory(), new EdgeFactory(), nodes, probability);
		return erdosRenyiGenerator.create();
	}


	public Graph<Node, Edge> create(RandomGraphType randomGraphType, Properties properties) {
		Graph<Node, Edge> graph = null;
		switch (randomGraphType) {
		case RADNDOM:
			graph = generateRandomGraph(Integer.valueOf((String) properties.get("nodes")), Integer.valueOf((String) properties.get("edges")));
			break;

		case ERDOSRENYI:
			graph = generateErdosRenyi(Integer.valueOf((String) properties.get("nodes")), Double.valueOf((String) properties.get("prob")));
			break;
			
		default:
			graph = new UndirectedSparseGraph<>();
			break;
		}
		return graph;
	}

}
