package pl.edu.pw.elka.community.finding.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import edu.uci.ics.jung.algorithms.matrix.GraphMatrixOperations;
import edu.uci.ics.jung.graph.Graph;

public class FastNewman<V, E> implements Algorithm<V, E> {

	private Map<Integer, Set<V>> nodesIDsMap;

	private SparseDoubleMatrix2D adjacencyGroup;

	private ArrayList<DendrogramLevel<V>> dendrogram;

	private int m;

	/**
	 * Global value of modularity.
	 */
	private double Q = 0;

	@Override
	public Set<Set<V>> getCommunities(Graph<V, E> graph) {
		nodesIDsMap = new HashMap<>();
		adjacencyGroup = GraphMatrixOperations.graphToSparseMatrix(graph);
		m = graph.getEdgeCount();
		dendrogram = new ArrayList<>();

		// every node into different group
		int id = 0;
		for (V v : graph.getVertices()) {
			HashSet<V> group = new HashSet<V>();
			group.add(v);
			nodesIDsMap.put(id++, group);
		}

		addDendrogramLevel(calculateModularity());
		System.out.println("Init Q: " + Q + " m:" + m);

		/*
		 * Calculate possible agglomerations.
		 */
		while (nodesIDsMap.size() > 1) {
			System.out.println("w macierzy:" + adjacencyGroup.rows() + " w mapie:" + nodesIDsMap.size());
			System.out.println(nodesIDsMap);
			System.out.println(adjacencyGroup);
			System.out.println("Q:" + Q + " Q2:" + calculateModularity());

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
				addDendrogramLevel(bestDQ);
			}
			System.out.println("Q:" + Q);
		}

		/*
		 * Chose best partition.
		 */
		double maxQ = -Double.MAX_VALUE;
		DendrogramLevel<V> chosenOne = null;
		for (DendrogramLevel<V> dl : dendrogram) {
			System.out.println("Q:" + dl.getQ() + " size:" + dl.getGroups().size());
			if (dl.getQ() > maxQ) {
				maxQ = dl.getQ();
				chosenOne = dl;
			}
		}

		System.out.println("Wybrany podział: " + chosenOne);

		return chosenOne.getGroups();
	}

	private double delataQ(int i, int j) {
		double eii = adjacencyGroup.getQuick(i, i) / m;
		double ejj = adjacencyGroup.getQuick(j, j) / m;
		System.out.println(adjacencyGroup.viewRow(i));
		double ai = adjacencyGroup.viewRow(i).zSum() / m - eii;
		System.out.println(adjacencyGroup.viewRow(j));
		double aj = adjacencyGroup.viewRow(j).zSum() / m - ejj;
		// double ai = 0;
		// double aj = 0;
		// for (int k = 0; k < adjacencyGroup.viewColumn(i).size(); k++) {
		// if (k != i) {
		// ai += adjacencyGroup.viewColumn(i).get(k);
		// }
		// if (k != j) {
		// aj += adjacencyGroup.viewColumn(j).get(k);
		// }
		// }
		// ai = ai / m;
		// aj = aj / m;

		double eij = adjacencyGroup.getQuick(i, j) / m;
		System.out.println("[" + i + ", " + j + "] eii:" + eii + " ejj:" + ejj + " eij:" + eij + " ai:" + ai + " aj:" + aj + " dQ:" + 2 * (eij - ai * aj));
		return 2 * (eij - ai * aj);
	}

	private void mergeGroups(int ci, int cj) {
		int i;
		int j;
		if (ci < cj) { // ci should be less than cj, otherwise nodesIDsMap will lose integrity
			i = ci;
			j = cj;
		} else {
			j = ci;
			i = cj;
		}

		System.out.println("i:" + i + " j:" + j);

		/*
		 * Merging the bidiMap.
		 */
		nodesIDsMap.get(i).addAll(nodesIDsMap.get(j));
		nodesIDsMap.remove(j);

		// creating new map
		Collection<Set<V>> n = nodesIDsMap.values();
		nodesIDsMap = new HashMap<Integer, Set<V>>();
		int groupNumber = 0;
		for (Set<V> grp : n) {
			if (!grp.isEmpty()) {
				nodesIDsMap.put(groupNumber++, grp);
			}
		}

		/*
		 * Merging the group adjacency matrix.
		 */

		// updating values inside matrix
		double newiiValue = adjacencyGroup.getQuick(i, i) + adjacencyGroup.getQuick(j, j) + adjacencyGroup.getQuick(i, j);
		for (int row = 0; row < adjacencyGroup.rows(); row++) {
			if (row != i) {
				adjacencyGroup.setQuick(row, i, adjacencyGroup.getQuick(row, j) + adjacencyGroup.getQuick(row, i));
			}
		}
		for (int col = 0; col < adjacencyGroup.columns(); col++) {
			if (col != i) {
				adjacencyGroup.setQuick(i, col, adjacencyGroup.getQuick(j, col) + adjacencyGroup.getQuick(i, col));
			}
		}
		adjacencyGroup.setQuick(i, i, newiiValue);

		// put zeros where unused group
		// for (int row = 0; row < adjacencyGroup.rows(); row++) {
		// adjacencyGroup.setQuick(row, j, 0);
		// }
		//
		// for (int col = 0; col < adjacencyGroup.columns(); col++) {
		// adjacencyGroup.setQuick(j, col, 0);
		// }

		// remove not used columns & rows
		int size = adjacencyGroup.rows() - 1;
		int[] rowsCols = new int[size];
		for (int idx = 0, val = 0; idx < size; val++, idx++) {
			if (val == j) {
				rowsCols[idx] = ++val;
			} else {
				rowsCols[idx] = val;
			}
		}

		adjacencyGroup = new SparseDoubleMatrix2D(adjacencyGroup.viewSelection(rowsCols, rowsCols).toArray());
	}

	private void addDendrogramLevel(double valueQ) {
		Q += valueQ;
		Set<Set<V>> level = new HashSet<Set<V>>();
		for (Set<V> group : nodesIDsMap.values()) {
			level.add(new HashSet<V>(group));
		}
		dendrogram.add(new DendrogramLevel<V>(Q, level));
	}

	private double calculateModularity() {
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

	public class DendrogramLevel<V> {
		private Double q;
		private Set<Set<V>> groups;

		public DendrogramLevel(Double q, Set<Set<V>> groups) {
			this.q = q;
			this.groups = groups;
		}

		public Double getQ() {
			return q;
		}

		public Set<Set<V>> getGroups() {
			return groups;
		}

		@Override
		public String toString() {
			return "DendrogramLevel <q=" + q + " " + groups + ">";
		}
	}
}
