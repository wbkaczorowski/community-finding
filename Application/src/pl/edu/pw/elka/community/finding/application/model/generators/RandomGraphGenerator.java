package pl.edu.pw.elka.community.finding.application.model.generators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import pl.edu.pw.elka.community.finding.application.model.graph.EdgeFactory;
import pl.edu.pw.elka.community.finding.application.model.graph.GraphFactory;
import pl.edu.pw.elka.community.finding.application.model.graph.NodeFactory;
import pl.edu.pw.elka.community.finding.application.model.graph.UndirectedGraphFactory;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.generators.random.EppsteinPowerLawGenerator;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

/**
 * Generator of artificial graphs.
 * 
 * @author Wojciech Kaczorowski
 * 
 */
public class RandomGraphGenerator {

	private Random random;

	public RandomGraphGenerator() {
		random = new Random();
	}

	private Graph<Node, Edge> generateRandomGraph(int nodes, double density) {
		Graph<Node, Edge> graph = new UndirectedSparseGraph<>();
		for (int n = 0; n < nodes; ++n) {
			Properties properties = new Properties();
			properties.put("id", String.valueOf(n));
			Node node = new Node(properties);
			node.setGroup("0");
			graph.addVertex(node);
		}
		ArrayList<Node> vertices = new ArrayList<Node>(graph.getVertices());

		int edges = (int) ((density * vertices.size() * (vertices.size() - 1)) / 2);
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

	private Graph<Node, Edge> generateModularGraph(int nodes, int communities, double densityInside, double densityTotal) {
		ArrayList<ArrayList<Node>> structure = new ArrayList<>();
		for (int c = 0; c < communities; ++c) {
			ArrayList<Node> set = new ArrayList<Node>();
			structure.add(set);
		}

		Graph<Node, Edge> graph = new UndirectedSparseGraph<>();
		for (int n = 0; n < nodes; ++n) {
			Properties properties = new Properties();
			properties.put("id", String.valueOf(n));
			Node node = new Node(properties);
			node.setGroup("0");
			graph.addVertex(node);
			properties.put("artificial community", String.valueOf(n % communities));
			structure.get(n % communities).add(node);
		}
		// creating edges insides groups
		for (ArrayList<Node> comm : structure) {
			int commSize = comm.size();
			int edgesInside = (int) ((commSize * (commSize - 1) * densityInside) / 2);
			for (int e = 0; e < edgesInside; ++e) {
				while (true) {
					int firstNode = random.nextInt(commSize);
					int secondNode = random.nextInt(commSize);
					if (firstNode != secondNode && graph.findEdge(comm.get(firstNode), comm.get(secondNode)) == null) {
						graph.addEdge(new Edge(e), comm.get(firstNode), comm.get(secondNode));
						break;
					}
				}
			}
		}

		// creating edges between communities
		int edgesTotal = (int) (densityTotal * graph.getVertexCount() * (graph.getVertexCount() - 1) / 2);
		ArrayList<Node> vertices = new ArrayList<Node>(graph.getVertices());
//		System.out.println(edgesTotal + " " + graph.getEdgeCount());
		while (edgesTotal > graph.getEdgeCount()) {
			while (true) {
				Node firstNode = vertices.get(random.nextInt(nodes));
				Node secondNode = vertices.get(random.nextInt(nodes));
				if (!(firstNode.equals(secondNode)) && !sameCommunity(firstNode, secondNode, structure) && graph.findEdge(firstNode, secondNode) == null) {
					graph.addEdge(new Edge(edgesTotal--), firstNode, secondNode);
					break;
				}
			}
		}

		return graph;
	}

	private boolean sameCommunity(Node first, Node second, ArrayList<ArrayList<Node>> structure) {
		for (Collection<Node> community : structure) {
			if (community.contains(first) && community.contains(second)) {
				return true;
			}
		}
		return false;
	}

	private Graph<Node, Edge> generateBarabasiAlbert(int nodes, int edges) {
		Set<Node> seedVertices = new HashSet<>();
		for (int i = 0; i < nodes / 3; ++i) {
			Node n = new Node();
			seedVertices.add(n);
		}
		BarabasiAlbertGenerator<Node, Edge> generator = new BarabasiAlbertGenerator<Node, Edge>(new GraphFactory(), new NodeFactory(), new EdgeFactory(),
				nodes, edges, seedVertices);

		Graph<Node, Edge> graph = generator.create();
		for (Node n : graph.getVertices()) {
			n.setGroup("0");
		}
		return graph;
	}

	private Graph<Node, Edge> generateErdosRenyi(int nodes, double probability) {
		ErdosRenyiGenerator<Node, Edge> erdosRenyiGenerator = new ErdosRenyiGenerator<Node, Edge>(new UndirectedGraphFactory(), new NodeFactory(),
				new EdgeFactory(), nodes, probability);
		Graph<Node, Edge> graph = erdosRenyiGenerator.create();
		for (Node n : graph.getVertices()) {
			n.setGroup("0");
		}
		return graph;
	}

	private Graph<Node, Edge> generateEppstein(int nodes, int edges, int iterations) {
		EppsteinPowerLawGenerator<Node, Edge> generator = new EppsteinPowerLawGenerator<Node, Edge>(new GraphFactory(), new NodeFactory(), new EdgeFactory(),
				nodes, edges, iterations);
		Graph<Node, Edge> graph = generator.create();
		for (Node n : graph.getVertices()) {
			n.setGroup("0");
		}
		return graph;
	}

	private Graph<Node, Edge> generateKleinbergSmallWorld(int nodes, double clusteringExponent) {
		KleinbergSmallWorldGenerator<Node, Edge> generator = new KleinbergSmallWorldGenerator<Node, Edge>(new UndirectedGraphFactory(), new NodeFactory(),
				new EdgeFactory(), (int) Math.round(Math.sqrt(nodes)), -clusteringExponent);

		Graph<Node, Edge> graph = generator.create();
		for (Node n : graph.getVertices()) {
			n.setGroup("0");
		}
		return graph;
	}

	public Graph<Node, Edge> create(RandomGraphType randomGraphType, Properties properties) {
		Graph<Node, Edge> graph = null;
		switch (randomGraphType) {
		case RANDOM:
			graph = generateRandomGraph(Integer.valueOf((String) properties.get("nodes")), Double.valueOf((String) properties.get("density")) / 100);
			break;

		case RANDOMMODULAR:
			graph = generateModularGraph(Integer.valueOf((String) properties.get("nodes")), Integer.valueOf((String) properties.get("comm")),
					Double.valueOf((String) properties.get("densityInside")) / 100, Double.valueOf((String) properties.get("densityTotal")) / 100);
			break;

		case BARABASIALBERT:
			graph = generateBarabasiAlbert(Integer.valueOf((String) properties.get("nodes")), Integer.valueOf((String) properties.get("edges")));
			break;

		case ERDOSRENYI:
			graph = generateErdosRenyi(Integer.valueOf((String) properties.get("nodes")), Double.valueOf((String) properties.get("prob")));
			break;

		case EPPSTEIN:
			graph = generateEppstein(Integer.valueOf((String) properties.get("nodes")), Integer.valueOf((String) properties.get("edges")),
					Integer.valueOf((String) properties.get("iterations")));
			break;

		case KLEINBERGSMALLWORLD:
			graph = generateKleinbergSmallWorld(Integer.valueOf((String) properties.get("nodes")), Double.valueOf((String) properties.get("exp")));
			break;

		default:
			graph = new UndirectedSparseGraph<>();
			break;
		}
		return graph;
	}

}
