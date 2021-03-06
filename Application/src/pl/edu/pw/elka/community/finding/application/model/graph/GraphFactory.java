package pl.edu.pw.elka.community.finding.application.model.graph;

import org.apache.commons.collections15.Factory;

import pl.edu.pw.elka.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

/**
 * Factory for creating new graphs.
 * @author Wojciech Kaczorowski
 *
 */
public class GraphFactory implements Factory<Graph<Node, Edge>>{

	@Override
	public Graph<Node, Edge> create() {
		return new UndirectedSparseGraph<Node, Edge>();
	}

}
