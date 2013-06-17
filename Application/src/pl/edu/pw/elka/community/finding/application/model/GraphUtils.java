package pl.edu.pw.elka.community.finding.application.model;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pl.edu.pw.elka.community.finding.application.model.graph.EdgeFactory;
import pl.edu.pw.elka.community.finding.application.model.graph.NodeFactory;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphMLMetadata;
import edu.uci.ics.jung.io.GraphMLReader;
import edu.uci.ics.jung.io.PajekNetReader;

/**
 * Utilities for graphs.
 * 
 * @author Wojciech Kaczorowski
 * 
 */
public class GraphUtils {

	
	public static Graph<Node, Edge> read(String filename) throws IOException, ParserConfigurationException, SAXException {
		GraphMLReader<Graph<Node, Edge>, Node, Edge> graphMLReader;
		PajekNetReader<Graph<Node, Edge>, Node, Edge> pajekNetReader;
		Graph<Node, Edge> graph = null;
		
		if (filename.endsWith(".graphml")) {
			graphMLReader = new GraphMLReader<Graph<Node, Edge>, Node, Edge>(new NodeFactory(), new EdgeFactory());

			graph = new UndirectedSparseGraph<Node, Edge>();
			graphMLReader.load(filename, graph);

			Map<String, GraphMLMetadata<Node>> nodeMetadata = graphMLReader.getVertexMetadata();

			// setting data for nodes
			Set<String> keys = nodeMetadata.keySet();
			for (Node n : graph.getVertices()) {
				for (String key : keys) {
					if (!nodeMetadata.get(key).transformer.transform(n).equals("")) {
						n.getData().put(key, nodeMetadata.get(key).transformer.transform(n));
					}
				}
				// by default, at beginning all belongs to the same group
				n.setGroup("0");
			}
		} else if (filename.endsWith(".paj")) {
			pajekNetReader = new PajekNetReader<Graph<Node, Edge>, Node, Edge>(new NodeFactory(), new EdgeFactory());
			
			graph = new UndirectedSparseGraph<Node, Edge>();
			pajekNetReader.load(filename, graph);

			for (Node n : graph.getVertices()) {
				n.setGroup("0");
			}
		}
		return graph;
	}

	
	public static Graph<Node, Edge> makeCopy(Graph<Node, Edge> graph) {
		Graph<Node, Edge> copy = new UndirectedSparseGraph<Node, Edge>();
		for (Edge e : graph.getEdges()) {
			copy.addEdge(e, graph.getEndpoints(e));
		}
		return copy;
	}

}
