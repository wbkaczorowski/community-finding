package pl.edu.pw.elka.inz.socialalgorithms.app.model.algorithms;

import java.util.Set;

import pl.edu.pw.elka.inz.socialalgorithms.app.model.graph.structure.Edge;
import pl.edu.pw.elka.inz.socialalgorithms.app.model.graph.structure.Node;
import edu.uci.ics.jung.algorithms.cluster.BicomponentClusterer;
import edu.uci.ics.jung.graph.UndirectedGraph;

public class Bicomponent extends Algorithm {

	@Override
	public void calculate() {
		System.out.println("BicomponentCluster");
		BicomponentClusterer<Node, Edge> bicomponentClusterer = new BicomponentClusterer<Node, Edge>();
		int i = 0;
		for (Set<Node> s :bicomponentClusterer.transform((UndirectedGraph<Node, Edge>) graph)) {
			System.out.println("Grupa nr: " + i);
			for (Node n : s) {
				n.setGroup(String.valueOf(i));
				System.out.println(n.toString());

			}
			++i;
		}
		

	}

}
