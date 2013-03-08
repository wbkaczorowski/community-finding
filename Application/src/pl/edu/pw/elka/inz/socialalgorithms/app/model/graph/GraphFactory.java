package pl.edu.pw.elka.inz.socialalgorithms.app.model.graph;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

import pl.edu.pw.elka.inz.socialalgorithms.app.model.graph.structure.Edge;
import pl.edu.pw.elka.inz.socialalgorithms.app.model.graph.structure.Node;

public class GraphFactory implements Factory<UndirectedGraph<Node, Edge>>{

	@Override
	public UndirectedGraph<Node, Edge> create() {
		return new UndirectedSparseGraph<Node, Edge>();
	}

}
