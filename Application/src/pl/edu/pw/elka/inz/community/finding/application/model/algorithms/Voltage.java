package pl.edu.pw.elka.inz.community.finding.application.model.algorithms;

import java.util.Set;

import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.algorithms.cluster.VoltageClusterer;

public class Voltage extends Algorithm {

	@Override
	public void calculate() {
		System.out.println("VoltageClusterer");
		VoltageClusterer<Node, Edge> voltageClusterer = new VoltageClusterer<Node, Edge>(graph, 10);
		int i = 0;
		for (Set<Node> s : voltageClusterer.cluster(10)) {
//			System.out.println("Grupa nr: " + i);
			for (Node n : s) {
				n.setGroup(String.valueOf(i));
//				System.out.println(n.toString());

			}
			++i;
		}
	}

}
