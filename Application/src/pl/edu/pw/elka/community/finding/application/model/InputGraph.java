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
		if (filename.endsWith(".graphml")) {
			graphMLFile(filename);
		} else if (filename.endsWith(".paj")) {
			pajekFile(filename);
		}
	}
	
	private void graphMLFile(String filename) throws ParserConfigurationException, SAXException, IOException {
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
	}
	
	private void pajekFile(String filename) {
		
	}

	public Graph<Node, Edge> getGraph() {
		return graph;
	}

}
