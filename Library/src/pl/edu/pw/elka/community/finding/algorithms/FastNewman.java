package pl.edu.pw.elka.community.finding.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import edu.uci.ics.jung.algorithms.matrix.GraphMatrixOperations;
import edu.uci.ics.jung.graph.Graph;

/**
 * Greedy method for finding communities in graph structures. Based on method proposed in article written by M. E. J. Newman, Fast algorithm for detecting
 * community structure in networks, Physical Review E, Vol. 69, No. 6. (Jun 2004).
 * 
 * @author Wojciech Kaczorowski
 * 
 * @param <V> vertex
 * @param <E> edge
 */
public class FastNewman<V, E> implements Algorithm<V, E> {

	/**
	 * Mapping groups ids on sets representing chosen groups.
	 */
	private Map<Integer, Set<V>> nodesIDsMap;

	/**
	 * Indices of groups inside adjacency matrix.
	 */
	private Map<Integer, Set<Integer>> groupIndicesMap;

	/**
	 * Graph adjacency matrix.
	 */
	private SparseDoubleMatrix2D adjacencyGroup;

	/**
	 * Dendrogram built by next joins of groups.
	 */
	private ArrayList<DendrogramLevel<V>> dendrogram;

	/**
	 * Number of edges of graph.
	 */
	private int m;

	private Algebra algebra;

	public FastNewman() {
		algebra = new Algebra();
	}

	@Override
	public Set<Set<V>> getCommunities(Graph<V, E> graph) {
		nodesIDsMap = new HashMap<>();
		adjacencyGroup = GraphMatrixOperations.graphToSparseMatrix(graph);
		m = graph.getEdgeCount();
		dendrogram = new ArrayList<>();
		groupIndicesMap = new HashMap<Integer, Set<Integer>>();

		// every node into different group
		int id = 0;
		for (V v : graph.getVertices()) {
			HashSet<V> group = new HashSet<V>();
			group.add(v);
			HashSet<Integer> groupIndex = new HashSet<Integer>();
			groupIndex.add(id);
			groupIndicesMap.put(id, groupIndex);
			nodesIDsMap.put(id++, group);
		}

		// first level of dendrogram
		addDendrogramLevel();

		System.out.println(adjacencyGroup);

		/*
		 * Calculate possible agglomerations.
		 */
		while (nodesIDsMap.size() > 1) {
			double bestDQ = -Double.MAX_VALUE;
			int[] groupsToMerge = null;

			for (int outsideID : groupIndicesMap.keySet()) {
				for (int insideID : groupIndicesMap.keySet()) {
					// check if two groups are not the same and are connected by edge
					if (outsideID != insideID && areConnected(outsideID, insideID)) {
						double dQ = deltaQ(outsideID, insideID);
						if (dQ > bestDQ) {
							bestDQ = dQ;
							groupsToMerge = new int[] { outsideID, insideID };
						}
					}
				}
			}
			if (groupsToMerge != null) {
				mergeGroups(groupsToMerge[0], groupsToMerge[1]);
				addDendrogramLevel();
			}
		}

		/*
		 * Chose best partition.
		 */
		double maxQ = -Double.MAX_VALUE;
		DendrogramLevel<V> chosenOne = null;
		for (DendrogramLevel<V> dl : dendrogram) {
			// System.out.println("Q:" + dl.getQ() + " size:" + dl.getGroups().size());
			if (dl.getQ() > maxQ) {
				maxQ = dl.getQ();
				chosenOne = dl;
			}
		}

		// System.out.println("Best partition: " + chosenOne);

		return chosenOne.getGroups();
	}

	/**
	 * Calculates the difference of modularity obtained by joining two communities.
	 * @param i id of first group
	 * @param j id of second group
	 * @return 
	 */
	private double deltaQ(int i, int j) {
		double eii = sumEdgesValues(adjacencyGroup.viewSelection(getIntArrayFromSet(groupIndicesMap.get(i)), getIntArrayFromSet(groupIndicesMap.get(i)))) / m;
		double ejj = sumEdgesValues(adjacencyGroup.viewSelection(getIntArrayFromSet(groupIndicesMap.get(j)), getIntArrayFromSet(groupIndicesMap.get(j)))) / m;
		double ai = viewRows(adjacencyGroup, getIntArrayFromSet(groupIndicesMap.get(i))).zSum() / m - eii;
		double aj = viewRows(adjacencyGroup, getIntArrayFromSet(groupIndicesMap.get(j))).zSum() / m - ejj;
		double eij = sumEdgesValues(adjacencyGroup.viewSelection(getIntArrayFromSets(groupIndicesMap.get(i), groupIndicesMap.get(j)),
				getIntArrayFromSets(groupIndicesMap.get(i), groupIndicesMap.get(j))))
				/ m - eii - ejj;

		// System.out.println("[" + i + ", " + j + "] eii:" + eii + " ejj:" + ejj + " eij:" + eij + " ai:" + ai + " aj:" + aj + " dQ:" + 2 * (eij - ai * aj));
		return 2 * (eij - ai * aj);
	}

	/**
	 * 
	 * @param i id of first group
	 * @param j id of second group
	 */
	private void mergeGroups(int i, int j) {
		nodesIDsMap.get(i).addAll(nodesIDsMap.get(j));
		nodesIDsMap.remove(j);
		groupIndicesMap.get(i).addAll(groupIndicesMap.get(j));
		groupIndicesMap.remove(j);
	}

	/**
	 * Builds one dendrogram level.
	 */
	private void addDendrogramLevel() {
		Set<Set<V>> level = new HashSet<Set<V>>();
		for (Set<V> group : nodesIDsMap.values()) {
			level.add(new HashSet<V>(group));
		}
		dendrogram.add(new DendrogramLevel<V>(calculateModularity(), level));
	}

	/**
	 * Calculates modularity Q of current partition.
	 * @return
	 */
	private double calculateModularity() {
		double initialQ = 0.0;
		double eii = 0.0;
		double ai = 0.0;

		for (Set<Integer> group : groupIndicesMap.values()) {
			eii = sumEdgesValues(adjacencyGroup.viewSelection(getIntArrayFromSet(group), getIntArrayFromSet(group))) / m;
			ai = viewRows(adjacencyGroup, getIntArrayFromSet(group)).zSum() / m - eii;
			initialQ += (eii - ai * ai);
		}

		return initialQ;
	}

	/**
	 * @param i id of first group
	 * @param j id of second group
	 * @return ture if two groups are connected by edge
	 */
	private boolean areConnected(int i, int j) {
		if (sumEdgesValuesWithoutTrace(adjacencyGroup.viewSelection(getIntArrayFromSets(groupIndicesMap.get(i), groupIndicesMap.get(j)),
				getIntArrayFromSets(groupIndicesMap.get(i), groupIndicesMap.get(j)))) == 0) {
			return false;
		} else {
			return true;
		}
	}

	private int[] getIntArrayFromSet(Set<Integer> set) {
		int[] array = new int[set.size()];
		int idx = 0;
		for (Integer i : set) {
			array[idx++] = i;
		}
		return array;
	}

	private int[] getIntArrayFromSets(Set<Integer> set1, Set<Integer> set2) {
		int[] array = new int[set1.size() + set2.size()];
		int idx = 0;
		for (Integer i : set1) {
			array[idx++] = i;
		}
		for (Integer i : set2) {
			array[idx++] = i;
		}
		return array;
	}

	private double sumEdgesValues(DoubleMatrix2D matrix) {
		double trace = algebra.trace(matrix);
		return (matrix.zSum() - trace) / 2 + trace;
	}

	private double sumEdgesValuesWithoutTrace(DoubleMatrix2D matrix) {
		return (matrix.zSum() - algebra.trace(matrix)) / 2;
	}

	private DoubleMatrix2D viewRows(DoubleMatrix2D matrix, int[] rows) {
		int[] columns = new int[matrix.columns()];
		for (int i = 0; i < matrix.columns(); ++i) {
			columns[i] = i;
		}
		return matrix.viewSelection(rows, columns);
	}
}
