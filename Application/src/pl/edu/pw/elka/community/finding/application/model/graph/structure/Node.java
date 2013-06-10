package pl.edu.pw.elka.community.finding.application.model.graph.structure;

import java.util.Properties;

/**
 * Node of graph.
 * 
 * @author Wojciech Kaczorowski
 * 
 */
public class Node {

	/**
	 * Data for easy identification of node.
	 */
	private Properties data;

	/**
	 * Id of group.
	 */
	private String group;

	public Node() {
		this.data = new Properties();
	}

	public Node(Properties data) {
		this.data = data;
	}

	public Properties getData() {
		return data;
	}

	public void setData(Properties data) {
		this.data = data;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "Node [" + data + ", group=" + group + "]";
	}

}
