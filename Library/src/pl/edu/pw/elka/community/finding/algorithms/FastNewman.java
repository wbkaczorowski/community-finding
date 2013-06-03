package pl.edu.pw.elka.community.finding.algorithms;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.bidimap.DualHashBidiMap;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

import edu.uci.ics.jung.algorithms.matrix.GraphMatrixOperations;
import edu.uci.ics.jung.algorithms.matrix.MatrixElementOperations;
import edu.uci.ics.jung.graph.Graph;

public class FastNewman<V, E> implements Algorithm<V, E> {

	private BidiMap<Integer, Set<V>> nodesIDsMap;

	private Map<Double, Set<Set<V>>> dendrogram;

	private SparseDoubleMatrix2D adjacencyGroup;

	private int m;

	private double Q;

	@Override
	public Set<Set<V>> getCommunities(Graph<V, E> graph) {
		nodesIDsMap = new DualHashBidiMap<>();
		dendrogram = new TreeMap<Double, Set<Set<V>>>();
		adjacencyGroup = GraphMatrixOperations.graphToSparseMatrix(graph);
		m = graph.getEdgeCount();

		// initial value of modularity
		Q = calculateInitModularity();
		System.out.println("Init Q: " + Q);

		// every node into different group
		int id = 0;
		for (V v : graph.getVertices()) {
			HashSet<V> group = new HashSet<V>();
			group.add(v);
			nodesIDsMap.put(id++, group);
		}

		System.out.println(adjacencyGroup);

		/*
		 * Calculate possible agglomerations.
		 */
		while (adjacencyGroup.rows() > 1) {
			// System.out.println(nodesIDsMap.size());
			// System.out.println(nodesIDsMap);
			// System.out.println(adjacencyGroup);

			double bestDQ = -Double.MAX_VALUE;
			int[] groupsToMerge = null;
			id = adjacencyGroup.rows();
			for (int row = 0; row < id; row++) {
				for (int column = row + 1; column < id; column++) {
					// check if two groups are connected by edge
					if (adjacencyGroup.get(row, column) != 0) {
						double dQ = delataQ(row, column);
						// System.out.println("badamy: [" + row + ", " + column + "] dQ: " + dQ);
						if (dQ > bestDQ) {
							// System.out.println("[" + row + ", " + column + "] dQ: " + dQ);
							bestDQ = dQ;
							groupsToMerge = new int[] { row, column };
						}
					}
				}
			}
			if (groupsToMerge != null) {
				mergeGroups(groupsToMerge[0], groupsToMerge[1]);
				createDendrogramLevel(bestDQ);
			}
		}

		Double maxQ = -Double.MAX_VALUE;
		for (Double d : dendrogram.keySet()) {
			if (d > maxQ) {
				maxQ = d;
			}
		}

		// System.out.println(dendrogram);

		return dendrogram.get(maxQ);
	}

	private double delataQ(int i, int j) {
		double eii = adjacencyGroup.getQuick(i, i) / m;
		double ejj = adjacencyGroup.getQuick(j, j) / m;
		double ai = adjacencyGroup.viewRow(i).zSum() / m - eii;
		double aj = adjacencyGroup.viewRow(j).zSum() / m - ejj;
		double eij = adjacencyGroup.getQuick(i, j) / m;
		// System.out.println("[" + i + ", " + j + "] eii: " + eii + " ejj: " + ejj + " eij: " + eij + " ai:" + ai + " aj:" + aj);
		return 2 * (eij - ai * aj);
	}

	private void mergeGroups(int ci, int cj) {
		int i;
		int j;
		if (ci < cj) { // ci should be less than cj, otherwise bidimap will lose integrity
			i = ci;
			j = cj;
		} else {
			j = ci;
			i = cj;
		}

		/*
		 * Merging the bidiMap.
		 */
		// System.out.println(nodesIDsMap.get(i));
		// System.out.println(nodesIDsMap.get(j));
		nodesIDsMap.get(i).addAll(nodesIDsMap.get(j));
		nodesIDsMap.remove(j);

		/*
		 * Merging the group adjacency matrix.
		 */
		// System.out.println("i:" + i + " j:" + j);
		// System.out.println(adjacencyGroup);
		// updating values inside matrix
		for (int row = 0; row < adjacencyGroup.rows(); row++) {
			adjacencyGroup.setQuick(row, i, adjacencyGroup.getQuick(row, j) + adjacencyGroup.getQuick(row, i));
		}
		for (int col = 0; col < adjacencyGroup.columns(); col++) {
			if (col != i) {
				adjacencyGroup.setQuick(i, col, adjacencyGroup.getQuick(j, col) + adjacencyGroup.getQuick(i, col));
			}
		}

		// System.out.println(adjacencyGroup);

		// remove not used columns & rows
		int size = adjacencyGroup.rows() - 1;
		int[] rowsCols = new int[size];
		for (int idx = 0, val = 0; idx < size; val++, idx++) {
			if (val == j) {
				rowsCols[idx] = ++val;
			} else {
				rowsCols[idx] = val;
			}
			// System.out.print(rowsCols[idx] + " ");
		}
		// System.out.println("");
		adjacencyGroup = new SparseDoubleMatrix2D(adjacencyGroup.viewSelection(rowsCols, rowsCols).toArray());
	}

	private void createDendrogramLevel(double valueQ) {
		Q += valueQ;

		// creating new bidiMap
		Set<Set<V>> n = nodesIDsMap.values();
		nodesIDsMap = new DualHashBidiMap<Integer, Set<V>>();
		int idx = 0;
		for (Set<V> grp : n) {
			if (!grp.isEmpty()) {
				nodesIDsMap.put(idx++, grp);
			}
		}
		dendrogram.put(Q, nodesIDsMap.values());
		System.out.println("Q : " + Q);
	}

	private double calculateInitModularity() {
		double initialQ = 0.0;
		double eii = 0.0;
		double ai = 0.0;

		for (int row = 0; row < adjacencyGroup.rows(); row++) {
			eii = adjacencyGroup.get(row, row) / m;
			ai = adjacencyGroup.viewRow(row).zSum() / m - eii;
			initialQ += (eii - ai * ai);
		}

		return initialQ;
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
}
