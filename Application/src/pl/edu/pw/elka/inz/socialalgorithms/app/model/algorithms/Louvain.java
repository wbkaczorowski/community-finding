package pl.edu.pw.elka.inz.socialalgorithms.app.model.algorithms;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;

import pl.edu.pw.elka.inz.socialalgorithms.app.model.graph.structure.Node;

public class Louvain extends Algorithm {
	
	Transformer<Node, String> moduleMembership;
	
	
	public Louvain() {
		super();
		moduleMembership = new Transformer<Node, String>() {
			
			@Override
			public String transform(Node arg0) {
				return arg0.getGroup();
			}
		};
	}
	
	@Override
	public void calculate() {

		// ustawienie ze każdy węzeł w innej grupie 
		int groupNumber = 0;
		for (Node n : graph.getVertices()) {
			n.setGroup(String.valueOf(groupNumber++));			
		}
		
//		double modularity = Modularity.computeModularity(graph, moduleMembership);
//		double maxModularity = Modularity.computeMaxModularity(graph, moduleMembership);
		double scaledModularity = Modularity.computeScaledModularity(graph, moduleMembership);
		
//		System.out.println("Modualrity: " + modularity);
//		System.out.println("MaxModualrity: " + maxModularity);
//		System.out.println("ScaledModularity: " + scaledModularity);
		
		for (Node n : graph.getVertices()) {
			for (Node n2 : graph.getNeighbors(n)) {
				String oldGroup = n2.getGroup();
				n2.setGroup(n.getGroup());
				double acctualScaledModularity = Modularity.computeScaledModularity(graph, moduleMembership);
				if (acctualScaledModularity > scaledModularity) {
					scaledModularity = acctualScaledModularity;
				} else {
					n2.setGroup(oldGroup);
				}
			}
		}
		
//		for (Node n : graph.getVertices()) {
//			for (Node n2 : graph.getNeighbors(n)) {
//				if (!n2.getGroup().equals(n.getGroup())) {
//					//TODO f2
//				}
//			}
//		}
		
		//TODO f2
		
	
		
	}
	

	
}
