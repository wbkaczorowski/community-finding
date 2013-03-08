package pl.edu.pw.elka.inz.socialalgorithms.app.model.graph.structure;

/**
 * Węzeł grafu. W modelu sieci odpowiada on użytkownikowi facebooka.
 * 
 * @author Wojciech Kaczorowski
 * 
 */
public class Node {

	/**
	 * Facebookowy user id.
	 */
	private String uid;

	/**
	 * Imie i nazwisko użytkownika.
	 */
	private String name;

	/**
	 * Identyfikator grupy do której należy.
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
