package pl.edu.pw.elka.inz.community.finding.application.model.algorithms;

import java.util.Set;

import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;

public class GirvanNewman extends Algorithm {

	@Override
	public void calculate() {
		System.out.println("GrivanNewman");
		
		EdgeBetweennessClusterer<Node, Edge> edgeBetweennessClusterer = new EdgeBetweennessClusterer<Node, Edge>(100);
		int i = 0;
		for (Set<Node> s : edgeBetweennessClusterer.transform(graph)) {
//			System.out.println("Grupa nr: " + i);
			for (Node n : s) {
				n.setGroup(String.valueOf(i));
//				System.out.println(n.toString());

			}
			++i;
		}
		
//		System.out.println(edgeBetweennessClusterer.getEdgesRemoved().size());
//		for (Edge e : edgeBetweennessClusterer.getEdgesRemoved()) {
//			graph.removeEdge(e);
//		}

	}

}
