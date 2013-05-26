package pl.edu.pw.elka.community.finding.algorithms;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.Transformer;
import pl.edu.pw.elka.community.finding.algorithms.Modularity;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class Louvain<V, E> implements Algorithm<V, E> {

	/**
	 * Communities find by algorithm.
	 */
	private HashSet<Set<V>> communities;

	/**
	 * Values of edges, need to calculate gain of modularity.
	 */
	private Map<E, Double> edgesValues;

	private Transformer<V, Set<V>> moduleMembership;

	// global parameters for calculation
	/**
	 * Doubled number of all edges in graph, should be: Doubled the sum of the weights of all the links in the network.
	 */
	private double m2;
	
	/**
	 * Number of made full two-phase iterations.
	 */
	private int totalIterations;

	public Louvain() {
		moduleMembership = new Transformer<V, Set<V>>() {

			@Override
			public Set<V> transform(V arg0) {
				return findVertexGroup(arg0);
			}
		};
	}

	@Override
	public Set<Set<V>> getCommunities(Graph<V, E> graph) {
		Graph<V, E> loopGraph = graph;
		edgesValues = new HashMap<E, Double>();

		// default - no edges values in the graph
		for (E e : loopGraph.getEdges()) {
			edgesValues.put(e, new Double(1));
		}

		totalIterations = 0;

		while (true) {
			totalIterations++;
			if (!phaseOne(loopGraph)) {
//				System.out.println("Warunek stopu osiągnięty");
				// System.out.println(loopGraph.getVertices());
				break;
			}
			loopGraph = phaseTwo(loopGraph);

//			System.out.println("Nowy graf");
//			for (V v : loopGraph.getVertices()) {
//				System.out.println("o " + v);
//			}
//			for (E e : loopGraph.getEdges()) {
//				System.out.println("---- " + e + " " + edgesValues.get(e) + " " + loopGraph.getEndpoints(e));
//			}
		}

		communities = new HashSet<>();
		for (V g : loopGraph.getVertices()) {
			communities.add(separateElements((Set<V>) g));
		}

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
//				System.out.println("dQ:" + dQ + " " + i + " --> " + neighbourGroup);
				if (dQ > maxDQ) {
					maxDQ = dQ;
					newGroup = neighbourGroup;
					gainOccurred = true;
				}
			}

			if (newGroup != null) {
//				double mQ = calcualteModualarity(graph);
//				System.out.println("Usuwam: " + i + " z grupy " + findVertexGroup(i));
				findVertexGroup(i).remove(i);
//				System.out.println("Dodaje do grupy: " + newGroup);
				newGroup.add(i);
//				double mdQ = calcualteModualarity(graph) - mQ;
//				System.out.println("gotowiec: " + mdQ + " moje po: " + calcualteDeltaModularity(i, newGroup, graph));
			}
		}

		// for (V i : graph.getVertices()) {
		// maxDQ = 0;
		// maxDQ = calcualteModualarity(graph);
		// for (V neighbor : graph.getNeighbors(i)) {
		// HashSet<V> originalGroup = (HashSet<V>) findVertexGroup(i);
		//
		// double Q = calcualteModualarity(graph);
		// System.out.println(Q);
		// double dQ2 = calcualteDeltaModularity(i, findVertexGroup(i), graph);
		// System.out.println("Usuwam: " + i + " z grupy " + findVertexGroup(i));
		// findVertexGroup(i).remove(i);
		// System.out.println("Dodaje do grupy: " + findVertexGroup(neighbor));
		// findVertexGroup(neighbor).add(i);
		// TODO dlaczego moje nie działa?
		// double dQ = roundDouble(calcualteDeltaModularity(i, findVertexGroup(neighbor), graph), 6);
		// double dQ = calcualteDeltaModularity(i, findVertexGroup(neighbor), graph);
		// System.out.println(calcualteModualarity(graph));
		// double dQ = roundDouble((calcualteModualarity(graph) - Q), 6);
		// double dQ1 = (calcualteModualarity(graph) - Q);
		// double dQ = roundDouble(calcualteModualarity(graph), 6);
		// System.out.println("dQ: " + dQ + "  dQ1:" + dQ1 + " dQ2:" + dQ2);

		/*
		 * Set previous graph and groups topology if modularity difference is less than before modification.
		 */
		// if (dQ <= maxDQ) { // no gain of modularity
		// System.out.println(" Powrót do poprzedniego stanu");
		// findVertexGroup(i).remove(i);
		// originalGroup.add(i);
		// } else { // there is a gain of modularity
		// System.out.println(" Zmiana zostaje");
		// maxDQ = dQ;
		// gainOccurred = true;
		// }
		// System.out.println("	Mamy grupy:");
		// for (Set<V> g : tmpGroups) {
		// System.out.println("		" + g);
		// }
		// System.out.println("maxDQ: " + maxDQ);
		// }
		// }

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

	private double calcualteDeltaModularity(V i, Set<V> groupC, Graph<V, E> graph) {
		double dQfull = 0;
		double dQshort = 0;
		double sumIn = 0;
		double sumTot = 0;
		double ki = 0;
		double kiIn = 0;

		// TODO zbieranie wartości edge
		for (V v1 : groupC) {
//			for (V v2 : groupC) {
//				E e = graph.findEdge(v1, v2);
//				if (e != null) {
//					// System.out.println("		ba bum:" + edgesValues.get(e));
//					sumIn += 0.5 * edgesValues.get(e);
//				}
//			}

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

//		System.out.println(i + " sumIn:" + sumIn + " sumTot:" + sumTot + " ki:" + ki + " kiIn:" + kiIn + " m:" + m2 / 2);

		// dQfull = ((sumIn + kiIn) / m2 - ((sumTot + ki) / m2) * ((sumTot + ki) / m2)) - ((sumIn / m2) - (sumTot / m2) * (sumTot / m2) - (ki / m2) * (ki /
		// m2));
		dQshort = (kiIn / m2) - (2 * sumTot * ki / (m2 * m2));
		// dQshort = (kiIn / m2) - (sumTot * sumTot / (m2 * m2));
		// System.out.println("		dQ: " + dQfull + " dQ2:" + dQshort);
		return dQshort;
	}

	private double calcualteModualarity(Graph<V, E> graph) {
		return Modularity.computeScaledModularity(graph, moduleMembership);
		// return Modularity.computeModularity(graph, moduleMembership);
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

	private double roundDouble(double number, int decimalPlace) {
		int dec = 1;
		for (int i = 0; i < decimalPlace; ++i) {
			dec *= 10;
		}
		return (double) Math.round(number * dec) / dec;
	}

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
	
	public int getTotalIterations() {
		return totalIterations;
	}

}
