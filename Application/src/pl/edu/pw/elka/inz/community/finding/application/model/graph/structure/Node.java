package pl.edu.pw.elka.inz.community.finding.application.model.graph.structure;

/**
 * Node of graph. In model of network correspond to facebook user.
 * 
 * @author Wojciech Kaczorowski
 * 
 */
public class Node {

	/**
	 * Facebook unique user id.
	 */
	private String uid;

	/**
	 * Name of user.
	 */
	private String name;

	/**
	 * Id of group.
	 */
	private String group;

	public Node() {

	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "Node [uid=" + uid + ", name=" + name + ", group=" + group + "]";
	}

}
