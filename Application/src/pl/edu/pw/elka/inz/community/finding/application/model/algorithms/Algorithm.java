package pl.edu.pw.elka.inz.community.finding.application.model.algorithms;

import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.graph.Graph;

public abstract class Algorithm {
	
	protected Graph<Node, Edge> graph;

	public Graph<Node, Edge> getGraph() {
		return this.graph;
	}

	public void setGraph(Graph<Node, Edge> graph) {
		this.graph = graph;
	}

	public abstract void calculate();

}
