package pl.edu.pw.elka.community.finding.algorithms;

import java.util.Set;

import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

/**
 * Method for finding communities in graph structures. It is slightly improved version of EdgeBetweennessClusterer from JUNG, adapted to look for specified
 * number of communities. Based on article written by Michelle Girvan and M. E. J. Newman: Community structure in social and biological networks Proceedings of
 * the National Academy of Sciences, 99(12):7821--7826, June 11 2002.
 * 
 * @author Wojciech Kaczorowski
 * 
 * @param <V>
 *            vertex
 * @param <E>
 *            edge
 */
public class GrivanNewman<V, E> implements Algorithm<V, E> {

	/**
	 * Suggested number of candidates we want to receive as a result of algorithm partition.
	 */
	private int groupCandidates;

	public GrivanNewman(int groupCandidates) {
		this.groupCandidates = groupCandidates;
	}

	@Override
	public Set<Set<V>> getCommunities(Graph<V, E> graph) {
		// Like in original method remove edges.
		EdgeBetweennessClusterer<V, E> edgeBetweennessClusterer = new EdgeBetweennessClusterer<V, E>(graph.getEdgeCount() - selfLoops(graph));
		WeakComponentClusterer<V, E> weakComponentClusterer = new WeakComponentClusterer<V, E>();

		Set<Set<V>> groups = edgeBetweennessClusterer.transform(graph);
		if (groups.size() == groupCandidates) {
			return groups;
		}
		Graph<V, E> graphCopy = makeCopy(graph);
		for (E e : edgeBetweennessClusterer.getEdgesRemoved()) {
			graphCopy.removeEdge(e);
			groups = weakComponentClusterer.transform(graphCopy);
			if (groups.size() >= groupCandidates) {
				return groups;
			}
		}
		return null;
	}

	private Graph<V, E> makeCopy(Graph<V, E> graph) {
		Graph<V, E> copy = new UndirectedSparseGraph<V, E>();
		for (E e : graph.getEdges()) {
			copy.addEdge(e, graph.getEndpoints(e));
		}
		return copy;
	}

	private int selfLoops(Graph<V, E> graph) {
		int selfLoops = 0;
		for (V v : graph.getVertices()) {
			if (graph.getNeighbors(v).contains(v)) {
				++selfLoops;
			}
		}
		return selfLoops;
	}
}
