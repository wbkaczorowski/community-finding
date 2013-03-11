package pl.edu.pw.elka.inz.community.finding.application.model;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pl.edu.pw.elka.inz.community.finding.application.model.graph.EdgeFactory;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.NodeFactory;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphMLMetadata;
import edu.uci.ics.jung.io.GraphMLReader;

/**
 * Reader for one graph from file.
 * 
 * @author Wojciech Kaczorowski
 * 
 */
public class InputGraph {

	private GraphMLReader<Graph<Node, Edge>, Node, Edge> graphMLReader;
	private Graph<Node, Edge> graph;

	public InputGraph(String filename) throws ParserConfigurationException, SAXException, IOException {
		this.graphMLReader = new GraphMLReader<Graph<Node, Edge>, Node, Edge>(new NodeFactory(), new EdgeFactory());

		// undirected graph meets the case of facebook data
		this.graph = new UndirectedSparseGraph<Node, Edge>();
		graphMLReader.load(filename, graph);

		Map<String, GraphMLMetadata<Node>> nodeMetadata = graphMLReader.getVertexMetadata();

		// setting data for nodes
		if (nodeMetadata.keySet().contains("uid")) {
			for (Node n : graph.getVertices()) {
				n.setUid(nodeMetadata.get("uid").transformer.transform(n));
				n.setName(nodeMetadata.get("name").transformer.transform(n));
				// by default, at beginning all belongs to the same group
				n.setGroup("0");
				// System.out.println(n.toString());
			}
		}

	}

	public Graph<Node, Edge> getGraph() {
		return graph;
	}

}
