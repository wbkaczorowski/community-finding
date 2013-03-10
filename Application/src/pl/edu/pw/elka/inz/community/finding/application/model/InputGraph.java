package pl.edu.pw.elka.inz.community.finding.application.model;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pl.edu.pw.elka.inz.community.finding.application.model.graph.EdgeFactory;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.GraphFactory;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.NodeFactory;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.generators.random.EppsteinPowerLawGenerator;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphMLMetadata;
import edu.uci.ics.jung.io.GraphMLReader;

public class InputGraph {

	private GraphMLReader<Graph<Node, Edge>, Node, Edge> graphMLReader;
	private Graph<Node, Edge> graph;

	public InputGraph(String filename) throws ParserConfigurationException, SAXException, IOException {
		this.graphMLReader = new GraphMLReader<Graph<Node, Edge>, Node, Edge>(new NodeFactory(), new EdgeFactory());
		this.graph = new UndirectedSparseGraph<Node, Edge>();
		graphMLReader.load(filename, graph);
	
		Map<String, GraphMLMetadata<Node>> nodeMetadata = graphMLReader.getVertexMetadata();
		
		// ustawianie danych dla wierzcholkow
		for (Node n : graph.getVertices()) {
			n.setUid(nodeMetadata.get("uid").transformer.transform(n));
			n.setName(nodeMetadata.get("name").transformer.transform(n));
			// na początku wszyscy należą do tej samej grupy
			n.setGroup("0");
//			System.out.println(n.toString());
		}

		
	}

	public Graph<Node, Edge> getGraph() {
//		Set<Node> set = new HashSet<Node>();
//		for (int i=0; i<3; ++i) {
//			Node n = new Node();
//			n.setUid(String.valueOf(i));
//			set.add(n);
//		}
//		BarabasiAlbertGenerator<Node, Edge> barabasiAlbertGenerator = new BarabasiAlbertGenerator<Node, Edge>(new GraphFactory(), new NodeFactory(), new EdgeFactory(), 100, 1500, set);

//		EppsteinPowerLawGenerator<Node, Edge> eppsteinPowerLawGenerator = new EppsteinPowerLawGenerator<Node, Edge>(new GraphFactory(), new NodeFactory(), new EdgeFactory(), 1000, 2000, 10);
		
//		ErdosRenyiGenerator<Node, Edge> erdosRenyiGenerator = new ErdosRenyiGenerator<Node, Edge>(new GraphFactory(), new NodeFactory(), new EdgeFactory(), 500, 0.01);
		
//		KleinbergSmallWorldGenerator<Node, Edge> kleinbergSmallWorldGenerator = new KleinbergSmallWorldGenerator<Node, Edge>(new GraphFactory(), new NodeFactory(), new EdgeFactory(), 4, 0.9);
//		graph = barabasiAlbertGenerator.create();
//		graph = kleinbergSmallWorldGenerator.create();
//		graph = eppsteinPowerLawGenerator.create();
//		graph = erdosRenyiGenerator.create();
		System.out.println(graph.getEdgeCount());
		System.out.println(graph.getVertexCount());
		return graph;
	}

}
