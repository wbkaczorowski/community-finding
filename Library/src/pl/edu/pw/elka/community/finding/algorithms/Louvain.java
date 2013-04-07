package pl.edu.pw.elka.community.finding.algorithms;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.uci.ics.jung.graph.Graph;

public class Louvain<V, E> implements Algorithm<V, E> {
	

	@Override
	public Set<Set<V>> getCommunities(Graph<V, E> graph) {
		HashSet<Set<V>> groups = new HashSet<>();
		
//		List<Set<V>> tmpGroups = new ArrayList<>();
		
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
		for (V i : graph.getVertices()) {
			for(V n : graph.getNeighbors(i)) {
				System.out.println(calcualteDeltaModularity(i, findVertexGroup(n, groups), graph));
			}
		}
		
		
//		Iterator<Set<V>> moduleIterator = tmpGroups.iterator();
//
//		while (moduleIterator.hasNext()) {
//			Set<V> module = moduleIterator.next();
//			for (V v : module) {
//				for (V n : graph.getNeighbors(v)) {
//					
//				}
//			}
//			if(module.isEmpty()) {
//				moduleIterator.remove();
//			}
//		}
//			
//		groups.addAll(tmpGroups);
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
				if(!groupC.contains(v3)) {
					++sumTot;
				}
			}
			if (graph.isNeighbor(i, v1)) {
				++kiIn;
			}
		}
		sumIn = sumIn/2;
		
		ki = graph.degree(i);
				
		System.out.println("	sumIn:" + sumIn + " sumTot:" + sumTot + " kiIn:" + kiIn + " m:" + m);
		
		dQ = ((sumIn + kiIn)/(2*m) - ((sumTot+ki)/(2*m))*((sumTot+ki)/(2*m))) - ((sumIn/(2*m)) - (sumTot/(2*m))*(sumTot/(2*m)) - (ki/(2*m))*(ki/(2*m)));
		return dQ;
	}
	
	private Set<V> findVertexGroup(V vertex, Collection<Set<V>> modules) {
		for (Set<V> m : modules) {
			if(m.contains(vertex)) {
				return m;
			}
		}
		return null;
	}

}
