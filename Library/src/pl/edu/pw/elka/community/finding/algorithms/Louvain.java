package pl.edu.pw.elka.community.finding.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

/**
 * Method for finding communities in graph structures. Based on article written by Vincent D. Blondel, Jean-Loup Guillaume, Renaud Lambiotte and Etienne
 * Lefebvre: Fast unfolding of communities in large networks. Journal of Statistical Mechanics: Theory and Experiment 2008 (10), P1000.
 * 
 * @author Wojciech Kaczorowski
 * 
 * @param <V> vertex
 * @param <E> edge
 */
public class Louvain<V, E> implements Algorithm<V, E> {

	/**
	 * Communities found by algorithm.
	 */
	private HashSet<Set<V>> communities;

	/**
	 * Values of edges, need to calculate gain of modularity.
	 */
	private Map<E, Double> edgesValues;

	// global parameters for calculation and experiments
	/**
	 * Doubled number of all edges in graph, should be: Doubled the sum of the weights of all the links in the network.
	 */
	private double m2;

	/**
	 * Number of made full two-phase iterations.
	 */
	private int totalIterations;

	public Louvain() {

	}

	@Override
	public Set<Set<V>> getCommunities(Graph<V, E> graph) {
		Graph<V, E> loopGraph = graph;
		edgesValues = new HashMap<E, Double>();
		totalIterations = 0;

		// default - no edges values in the graph
		for (E e : loopGraph.getEdges()) {
			edgesValues.put(e, new Double(1));
		}

		while (true) {
			if (!phaseOne(loopGraph)) {
				break;
			}

			loopGraph = phaseTwo(loopGraph);
			totalIterations++;
		}

		communities = new HashSet<>();
		for (V g : loopGraph.getVertices()) {
			communities.add(separateElements((Set<V>) g));
		}

		System.out.println("Total two-phase iterations:  " + totalIterations);
		return communities;
	}

	/**
	 * Phase I of algorithm. Connecting nodes, in initials modules.
	 * 
	 * @param graph
	 * @return true if there was a gain of modularity
	 */
	private boolean phaseOne(Graph<V, E> graph) {
		boolean gainOccurred = false;
		double maxDQ = 0;
		communities = new HashSet<>();

		m2 = 0;
		for (E e : graph.getEdges()) {
			m2 += edgesValues.get(e);
		}
		m2 *= 2;

		/*
		 * Setting every node in different group.
		 */
		for (V v : graph.getVertices()) {
			Set<V> group = new HashSet<>();
			group.add(v);
			communities.add(group);
		}

		for (V i : graph.getVertices()) {
			maxDQ = 0;
			Set<V> newGroup = null;
			// looking for best change
			for (Set<V> neighbourGroup : findNeighborGroups(i, graph)) {
				double dQ = calcualteDeltaModularity(i, neighbourGroup, graph);
				if (dQ > maxDQ) {
					maxDQ = dQ;
					newGroup = neighbourGroup;
					gainOccurred = true;
				}
			}

			if (newGroup != null) {
				findVertexGroup(i).remove(i);
				newGroup.add(i);
			}
		}

		return gainOccurred;
	}

	private Collection<Set<V>> findNeighborGroups(V i, Graph<V, E> graph) {
		HashSet<Set<V>> neighbourGroups = new HashSet<Set<V>>();
		for (V neighbour : graph.getNeighbors(i)) {
			for (Set<V> group : communities) {
				if (group.contains(neighbour) && !group.contains(i)) {
					neighbourGroups.add(group);
				}
			}
		}
		return neighbourGroups;
	}

	/**
	 * Phase II of algorithm, creating a temporary new graph based on previous groups.
	 * 
	 * @param graph
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Graph<V, E> phaseTwo(Graph<V, E> graph) {
		Graph<Set<V>, Integer> newGraph = new UndirectedSparseGraph<>();

		Map<E, Double> tmpEdgesValues = new HashMap<>();

		for (Set<V> g : communities) {
			if (!g.isEmpty()) {
				newGraph.addVertex(g);
			}
		}
		int edgeId = 0;

		for (Set<V> group : communities) {
			if (!group.isEmpty()) {
				for (V v : group) {
					for (E e : graph.getIncidentEdges(v)) {
						V neighbor = graph.getOpposite(v, e);

						if (group.contains(neighbor)) { // Inside group, make loop.
							try {
								Integer edge = newGraph.findEdge(group, group);
								if (edge == null) { // There isn't such loop in new graph.
									edge = new Integer(edgeId);
									if (newGraph.addEdge(edge, group, group)) {
										tmpEdgesValues.put((E) edge, edgesValues.get(graph.findEdge(v, neighbor)));
									} else {
										System.err.println("Error: adding new edge.");
									}
								} else { // Such loop exist.
									tmpEdgesValues.put((E) edge, tmpEdgesValues.get(edge) + edgesValues.get(graph.findEdge(v, neighbor)));
								}
							} catch (Throwable t) {
								t.printStackTrace();
							}
						} else { // Outside group, normal edge.
							try {
								Integer edge = newGraph.findEdge(group, findVertexGroup(neighbor));
								if (edge == null) { // There isn't such edge in new graph.
									edge = new Integer(edgeId);
									if (newGraph.addEdge(edge, group, findVertexGroup(neighbor))) {
										tmpEdgesValues.put((E) edge, 0.5 * edgesValues.get(graph.findEdge(v, neighbor)));
									} else {
										System.err.println("Error: adding new edge.");
									}
								} else { // Such edge exist.
									tmpEdgesValues.put((E) edge, tmpEdgesValues.get(edge) + 0.5 * edgesValues.get(graph.findEdge(v, neighbor)));
								}
							} catch (Throwable t) {
								t.printStackTrace();
							}
						}
						++edgeId;
					}
				}
			}
		}

		edgesValues = tmpEdgesValues;

		return (Graph<V, E>) newGraph;
	}

	private Collection<V> findOutgoingNodes(Graph<V, E> graph, Set<V> group) {
		Collection<V> nodes = new ArrayList<V>();
		for (V vertex : group) {
			for (V v : graph.getNeighbors(vertex)) {
				if (!group.contains(v)) {
					nodes.add(v);
				}
			}
		}
		return nodes;
	}

	/**
	 * Method for calculating the difference in modularity as proposed in article.
	 * 
	 * @param i
	 *            vertex for which one is checking modualrity gain
	 * @param groupC
	 *            to which group we move vertex i
	 * @param graph
	 * @return
	 */
	private double calcualteDeltaModularity(V i, Set<V> groupC, Graph<V, E> graph) {
		double dQshort = 0;
		double sumTot = 0;
		double ki = 0;
		double kiIn = 0;

		for (V v1 : groupC) {

			for (V v3 : graph.getNeighbors(v1)) {
				E e = graph.findEdge(v1, v3);
				if (groupC.contains(v3)) {
					sumTot += 0.5 * edgesValues.get(e);
				} else {
					sumTot += edgesValues.get(e);
				}
			}
			E e = graph.findEdge(i, v1);
			if (e != null) {
				kiIn += edgesValues.get(e);
			}
		}

		for (E e : graph.getIncidentEdges(i)) {
			ki += edgesValues.get(e);
		}
		dQshort = (kiIn / m2) - (2 * sumTot * ki / (m2 * m2));
		return dQshort;
	}

	private Set<V> findVertexGroup(V vertex) {
		for (Set<V> m : communities) {
			if (m.contains(vertex)) {
				return m;
			}
		}
		return null;
	}

	private Collection<E> findOutgoingEdges(Graph<V, E> graph, Set<V> group) {
		Collection<E> edges = new ArrayList<E>();
		for (V vertex : group) {
			for (V v : graph.getNeighbors(vertex)) {
				if (!group.contains(v)) {
					edges.add(graph.findEdge(v, vertex));
				}
			}
		}
		return edges;
	}

	/**
	 * Separating elements to unification of returning groups.
	 * 
	 * @param s
	 * @return
	 */
	private Set<V> separateElements(Set<V> s) {
		Set<V> set = new HashSet<>();
		for (V v : s) {
			if (v instanceof Set<?>) {
				set.addAll(separateElements((Set<V>) v));
			} else {
				set.add(v);
			}
		}
		return set;
	}

	/**
	 * @return How mony iterations was made.
	 */
	public int getTotalIterations() {
		return totalIterations;
	}

}
