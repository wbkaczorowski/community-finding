package pl.edu.pw.elka.inz.socialalgorithms.app.model.graph;

import org.apache.commons.collections15.Factory;

import pl.edu.pw.elka.inz.socialalgorithms.app.model.graph.structure.Edge;

public class EdgeFactory implements Factory<Edge> {

	private int n = 0;

	@Override
	public Edge create() {
		return new Edge(n++);
	}

}
