package pl.edu.pw.elka.community.finding.application.model.tests;

import java.util.Collection;
import java.util.Set;

import pl.edu.pw.elka.community.finding.application.model.graph.structure.Node;

public class Output {
	
	private String name;
	private Collection<Set<Node>> communities;
	private long time;
	private double modularity;

	public Output() {

	}

	public Output(Set<Set<Node>> communities, String name, long time, double modularity) {
		this.communities = communities;
		this.name = name;
		this.time = time;
		this.modularity = modularity;
	}

	@Override
	public String toString() {
		// TODO
		return "[name=" + name + ", communities=" + communities.size() + ", time=" + time + " ms , modularity=" + modularity + "]";
	}

	public Collection<Set<Node>> getCommunities() {
		return communities;
	}

	public void setCommunities(Collection<Set<Node>> collection) {
		this.communities = collection;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public void setModularity(double modularity) {
		this.modularity = modularity;
	}
}
