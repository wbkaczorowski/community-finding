package pl.edu.pw.elka.community.finding.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.uci.ics.jung.graph.Graph;

public class Louvain<V, E> implements Algorithm<V, E> {

	private HashSet<Set<V>> groups;

	@Override
	public Set<Set<V>> getCommunities(Graph<V, E> graph) {
		groups = new HashSet<>();

		// List<Set<V>> tmpGroups = new ArrayList<>();

		/*
		 * Setting every node in different group.
		 */
		for (V v : graph.getVertices()) {
			HashSet<V> initialSet = new HashSet<V>();
			initialSet.add(v);
			groups.add(initialSet);
		}

		/*
		 * Phase I. Connecting nodes, in initials modules.
		 */
		double maxDQ = 0;
		for (V i : graph.getVertices()) {
			maxDQ = 0;
			for (V n : graph.getNeighbors(i)) {
				HashSet<V> vertexGroup = (HashSet<V>) findVertexGroup(i);
				System.out.println("Usuwam: " + i + " z grupy " + findVertexGroup(i));
				findVertexGroup(i).remove(i);
				System.out.println("Dodaje do grupy: " + findVertexGroup(n));
				findVertexGroup(n).add(i);
				double dQ = calcualteDeltaModularity(i, findVertexGroup(n), graph);
				System.out.println(dQ);
				// set previous graph and groups topology if modularity difference is less than before modification
				if (dQ < maxDQ) { // no gain of modularity
					System.out.println(" Powrót do poprzedniego stanu");
					findVertexGroup(i).remove(i);
					vertexGroup.add(i);
					System.out.println("	Mamy grupy:");
					for (Set<V> g : groups) {
						System.out.println("		" + g);
					}
				} else { // there is a gain of modularity
					System.out.println(" Zmiana zostaje");
					maxDQ = dQ;
				}
				System.out.println("maxDQ: " + maxDQ);
			}
		}

		// Iterator<Set<V>> moduleIterator = tmpGroups.iterator();
		// Iterator<Set<V>> moduleIterator = groups.iterator();
		// while (moduleIterator.hasNext()) {
		// Set<V> module = moduleIterator.next();
		// if(module.isEmpty()) {
		// moduleIterator.remove();
		// }
		// }
		//
		// groups.addAll(tmpGroups);
		for (Set<V> g : groups) {
			System.out.println("	" + g);
		}

		System.out.println(groups.size());
		return groups;
	}

	private double calcualteDeltaModularity(V i, Set<V> groupC, Graph<V, E> graph) {
		double dQ = 0;
		double m = graph.getEdgeCount();
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

		System.out.println(i + " sumIn:" + sumIn + " sumTot:" + sumTot + " kiIn:" + kiIn + " m:" + m);

		dQ = ((sumIn + kiIn) / (2 * m) - ((sumTot + ki) / (2 * m)) * ((sumTot + ki) / (2 * m)))
				- ((sumIn / (2 * m)) - (sumTot / (2 * m)) * (sumTot / (2 * m)) - (ki / (2 * m)) * (ki / (2 * m)));
		return dQ;
	}

	private Set<V> findVertexGroup(V vertex) {
		for (Set<V> m : groups) {
			if (m.contains(vertex)) {
				return m;
			}
		}
		return null;
	}

}
