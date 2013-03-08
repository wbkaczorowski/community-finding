package pl.edu.pw.elka.inz.community.finding.application.model.algorithms;

import java.util.Set;

import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.graph.UndirectedGraph;

// TODO coś to nie działa 

public class WeakComponent extends Algorithm {

	@Override
	public void calculate() {
		System.out.println("WeakComponent");
		WeakComponentClusterer<Node, Edge> weakComponentClusterer = new WeakComponentClusterer<Node, Edge>();
		int i = 0;
		for (Set<Node> s : weakComponentClusterer.transform((UndirectedGraph<Node, Edge>) graph)) {
			System.out.println("Grupa nr: " + i);
			for (Node n : s) {
				n.setGroup(String.valueOf(i));
				System.out.println(n.toString());

			}
			++i;
		}
		
	}

}
