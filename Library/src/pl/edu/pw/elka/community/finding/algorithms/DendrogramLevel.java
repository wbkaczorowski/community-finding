package pl.edu.pw.elka.community.finding.algorithms;

import java.util.Set;

/**
 * Class representing one level of dendrogram.
 * @author Wojciech Kaczorowski
 *
 * @param <V>
 */
public class DendrogramLevel<V> {
	/**
	 * Value of modularity for partition given by {@link DendrogramLevel#groups}.
	 */
	private Double q;
	
	/**
	 * Partition of graph.
	 */
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
