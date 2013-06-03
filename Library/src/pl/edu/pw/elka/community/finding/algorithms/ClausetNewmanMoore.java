package pl.edu.pw.elka.community.finding.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import edu.uci.ics.jung.algorithms.matrix.GraphMatrixOperations;
import edu.uci.ics.jung.graph.Graph;

public class ClausetNewmanMoore<V, E> implements Algorithm<V, E> {

	/**
	 * Communities found by algorithm.
	 */
	private HashSet<Set<V>> communities;
	
	private HashMap<Set<V>, Integer> communitiesID;

	private ArrayList<TreeMap<Double, Set<V>>> deltaQ;

	private PriorityQueue<ElementH> H;

	private Vector<Double> ai;
	
	private double m2;

	@Override
	public Set<Set<V>> getCommunities(Graph<V, E> graph) {
		communities = new HashSet<>();
		communitiesID = new HashMap<>();
		
		initialValues(graph);

		System.out.println(GraphMatrixOperations.graphToSparseMatrix(graph));
		
		return communities;
	}
	
	private void initialValues(Graph<V, E> graph) {
		int id = 0;
		for (V v : graph.getVertices()) {
			HashSet<V> group = new HashSet<V>();
			group.add(v);
			communitiesID.put(group, id++);
		}
		deltaQ = new ArrayList<>(graph.getVertexCount());
		m2 = 2.0 * graph.getEdgeCount();
		
		for (V v : graph.getVertices()) {
			TreeMap<Double, Integer> dQj = new TreeMap<>();
			Collection<V> neighbors = graph.getNeighbors(v);
			for (V neighbour : neighbors) {
				double dQij = 1/m2 - neighbors.size() * graph.degree(neighbour) / (m2*m2);
				
//				dQj.put(dQij, neighbour);
			}
//			deltaQ.add(dQj);
		}
		
	}

	private void joinCommunities(int j, int i) {
//		communitiesID.get(j).addAll(communitiesID.get(i));
//		communitiesID.remove(i);
	}
	
	public class ElementH<V> {
		private Set<V> firstGroup;
		private Set<V> secondGroup;
		private double value;
		private int rowIndex;

		public Set<V> getFirstGroup() {
			return firstGroup;
		}

		public void setFirstGroup(Set<V> firstGroup) {
			this.firstGroup = firstGroup;
		}

		public Set<V> getSecondGroup() {
			return secondGroup;
		}

		public void setSecondGroup(Set<V> secondGroup) {
			this.secondGroup = secondGroup;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}

		public int getRowIndex() {
			return rowIndex;
		}

		public void setRowIndex(int rowIndex) {
			this.rowIndex = rowIndex;
		}
	}
}
