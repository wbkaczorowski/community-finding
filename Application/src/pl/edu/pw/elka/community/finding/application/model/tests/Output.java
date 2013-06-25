package pl.edu.pw.elka.community.finding.application.model.tests;

import java.util.Collection;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections15.Transformer;

import pl.edu.pw.elka.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.graph.Graph;

/**
 * Output from multiple test for single algorithm - single graph.
 * @author Wojciech Kaczorowski
 *
 */
public class Output {

	private Properties properties;
	private Collection<Set<Node>> communities;
	private long time;
	private double modularity;
	
	private Transformer<Node, Set<Node>> moduleMembership;

	public Output() {
		properties = new  Properties();
		moduleMembership = new Transformer<Node, Set<Node>>() {

			@Override
			public Set<Node> transform(Node arg0) {
				for (Set<Node> module : communities) {
					if (module.contains(arg0)) {
						return module;
					}
				}
				return null;
			}

		};
	}
	
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Adds to properties all other.
	 * @param properties
	 */
	public void addProperty(Properties properties) {
		this.properties.putAll(properties);
	}
	
	/**
	 * Puts properties.
	 * @param key
	 * @param value
	 */
	public void addProperty(String key, String value) {
		this.properties.put(key, value);
	}

	public void calculateModularity(Graph<Node, Edge> graph) {
		modularity = Modularity.computeScaledModularity(graph, moduleMembership);
	}
	
	@Override
	public String toString() {
		return properties + ", communities=" + communities.size() + ", time=" + time + " ms" + ", modularity=" + modularity;
	}
	
	public String toCSVline() {
		String string = "";
		for (Object o : properties.values()) {
			string += o + ",";
		}
		return string + communities.size() + "," + time + "," + modularity;
	}

	public Collection<Set<Node>> getCommunities() {
		return communities;
	}

	public void setCommunities(Collection<Set<Node>> collection) {
		this.communities = collection;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public double getModularity() {
		return modularity;
	}

}
