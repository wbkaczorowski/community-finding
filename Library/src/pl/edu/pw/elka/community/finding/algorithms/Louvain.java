package pl.edu.pw.elka.community.finding.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Phaser;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class Louvain<V, E> implements Algorithm<V, E> {

	private HashSet<Set<V>> mainGroups;
	private HashSet<Set<V>> tmpGroups;
	private Transformer<V, Set<V>> moduleMembership;

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
		mainGroups = new HashSet<>();

		Graph<V, E> loopGraph = graph;

		while (true) {
			if(!phaseOne(loopGraph)) {
				break;
			}
			
			loopGraph = phaseTwo(loopGraph);

			System.out.println("Nowy graf");
			for (V v : loopGraph.getVertices()) {
				System.out.println("o " + v);
			}

			for (E e : loopGraph.getEdges()) {
				System.out.println("----" + e + " " + loopGraph.getEndpoints(e));
			}
		}

		for (Set<V> g : mainGroups) {
			System.out.println("	" + g);
		}

		// System.out.println(groups.size());
		return mainGroups;
	}

	/*
	 * Phase I of algorithm. Connecting nodes, in initials modules.
	 */
	
	private boolean phaseOne(Graph<V, E> graph) {
		boolean gainOccurred = false;
		double maxDQ = 0;
		tmpGroups = new HashSet<>();
		
		/*
		 * Setting every node in different group.
		 */
		for (V v : graph.getVertices()) {
			Set<V> group = new HashSet<>();
			group.add(v);
			tmpGroups.add(group);
		}
		
		
		for (V i : graph.getVertices()) {
			maxDQ = 0;
			for (V neighbor : graph.getNeighbors(i)) {

				HashSet<V> vertexGroup = (HashSet<V>) findVertexGroup(i);
				double Q = calcualteModualarity(graph);
				// System.out.println("Usuwam: " + i + " z grupy " + findVertexGroup(i));
				findVertexGroup(i).remove(i);
				// System.out.println("Dodaje do grupy: " + findVertexGroup(neighbor));
				findVertexGroup(neighbor).add(i);
				// TODO dlaczego moje nie działa?
				// double dQ = calcualteDeltaModularity(i, findVertexGroup(neighbor), graph);

				double dQ = calcualteModualarity(graph) - Q;
				// System.out.println("dQ: " + dQ);

				// set previous graph and groups topology if modularity difference is less than before modification
				if (dQ < maxDQ) { // no gain of modularity
					// System.out.println(" Powrót do poprzedniego stanu");
					findVertexGroup(i).remove(i);
					vertexGroup.add(i);
					// System.out.println("	Mamy grupy:");
					// for (Set<V> g : groups) {
					// System.out.println("		" + g);
					// }
				} else { // there is a gain of modularity
					// System.out.println(" Zmiana zostaje");
					maxDQ = dQ;
					gainOccurred = true;
				}
				System.out.println("maxDQ: " + maxDQ);
			}
		}
		
		
		
		return gainOccurred;
	}

	/**
	 * Phase II of algorithm, creating a temporary new graph based on previous groups.
	 * @param graph
	 * @return
	 */
	private Graph<V, E> phaseTwo(Graph<V, E> graph) {
		Graph<String, String> newGraph = new UndirectedSparseGraph<>();
		for (Set<V> g : tmpGroups) {
			if (!g.isEmpty()) {
				newGraph.addVertex(g.toString());
			}
		}
		int edgeId = 0;
		for (Set<V> g : tmpGroups) {
			if (!g.isEmpty()) {
				for (E e : findOutgoingEdges(graph, g)) {
					try {
						newGraph.addEdge(String.valueOf(edgeId++), findVertexGroup(graph.getEndpoints(e).getFirst()).toString(),
								findVertexGroup(graph.getEndpoints(e).getSecond()).toString());
					} catch (Throwable t) {
						// TODO
					}
				}
			}
		}
		return (Graph<V, E>) newGraph;
	}

	private double calcualteDeltaModularity(V i, Set<V> groupC, Graph<V, E> graph) {
		double dQ = 0;
		double m2 = 2 * graph.getEdgeCount();
		double sumIn = 0;
		double sumTot = 0;
		double ki = 0;
		double kiIn = 0;

		for (V v1 : groupC) {
			for (V v2 : groupC) {
				if (graph.isNeighbor(v1, v2)) {
					++sumIn;
				}
			}

			for (V v3 : graph.getNeighbors(v1)) {
				if (!groupC.contains(v3)) {
					++sumTot;
				}
			}
			if (graph.isNeighbor(i, v1)) {
				++kiIn;
			}
		}
		sumIn = sumIn / 2;

		ki = graph.degree(i);

		System.out.println(i + " sumIn:" + sumIn + " sumTot:" + sumTot + " kiIn:" + kiIn + " m:" + m2 / 2);

		dQ = ((sumIn + kiIn) / m2 - ((sumTot + ki) / m2) * ((sumTot + ki) / m2)) - ((sumIn / m2) - (sumTot / m2) * (sumTot / m2) - (ki / m2) * (ki / m2));

		System.out.println("		dQ: " + dQ);
		return dQ;
	}

	private double calcualteModualarity(Graph<V, E> graph) {
		return Modularity.computeScaledModularity(graph, moduleMembership);
	}

	private Set<V> findVertexGroup(V vertex) {
		for (Set<V> m : tmpGroups) {
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
}
