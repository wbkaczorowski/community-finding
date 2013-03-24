package pl.edu.pw.elka.community.finding.application.model.graph.structure;

/**
 * Edge of graph.
 * @author Wojciech Kaczorowski
 *
 */
public class Edge {
	
	/**
	 * Unique id of edge. Describes one edge in one graph.
	 */
	private int id;

	public Edge(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Edge [id=" + id + "]";
	}
}
