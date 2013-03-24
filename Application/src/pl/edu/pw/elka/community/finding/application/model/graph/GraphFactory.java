package pl.edu.pw.elka.community.finding.application.model.graph;

import org.apache.commons.collections15.Factory;

import pl.edu.pw.elka.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

/**
 * Factory needed for reading from .graphml file
 * @author Wojciech Kaczorowski
 *
 */
public class GraphFactory implements Factory<UndirectedGraph<Node, Edge>>{

	@Override
	public UndirectedGraph<Node, Edge> create() {
		return new UndirectedSparseGraph<Node, Edge>();
	}

}
