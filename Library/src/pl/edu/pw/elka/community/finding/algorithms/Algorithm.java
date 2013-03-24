package pl.edu.pw.elka.community.finding.algorithms;

import java.util.Set;

import edu.uci.ics.jung.graph.Graph;

/**
 * Unifying interface for all implemented algorithms.
 * @author Wojciech Kaczorowski
 * @param <V> type for vertices of graph
 * @param <E> type for edges of graph
 *
 */
public interface Algorithm<V, E> {
	
	/**
	 * Get unique set of found groups, each set represent one group.
	 * @return 
	 */
	Set<Set<V>> getCommunities();
	
	/**
	 * Set graph for community finding.
	 * @param graph
	 */
	void setGraph(Graph<V, E> graph);
	
}
