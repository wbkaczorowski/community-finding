package pl.edu.pw.elka.inz.socialalgorithms.app.model.graph;

import org.apache.commons.collections15.Factory;

import pl.edu.pw.elka.inz.socialalgorithms.app.model.graph.structure.Node;

public class NodeFactory implements Factory<Node> {

	@Override
	public Node create() {
		return new Node();
	}

}
