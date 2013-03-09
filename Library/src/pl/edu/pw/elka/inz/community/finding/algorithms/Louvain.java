package pl.edu.pw.elka.inz.community.finding.algorithms;

import java.util.Set;

import edu.uci.ics.jung.graph.Graph;

public class Louvain<V, E> implements Algorithm<V, E> {

	private Graph<V, E> graph;
	
	@Override
	public Set<Set<V>> getCommunities() {
		Set<Set<V>> groups = null;
		
		
		
		return groups;
	}

	@Override
	public void setGraph(Graph<V, E> graph) {
		this.graph = graph;
	}

}
