package pl.edu.pw.elka.community.finding.application.model.tests;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import pl.edu.pw.elka.community.finding.application.model.algoritms.AlgorithmManager;
import pl.edu.pw.elka.community.finding.application.model.generators.RandomGraphGenerator;
import pl.edu.pw.elka.community.finding.application.model.generators.RandomGraphType;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.graph.Graph;

public class TestManager {

	private HashMap<String, Graph<Node, Edge>> testQueue;
	private AlgorithmManager algorithmManager;
	private RandomGraphGenerator randomGraphGenerator;
	private ArrayList<Output> results;

	public TestManager(AlgorithmManager am, RandomGraphGenerator rgg) {
		testQueue = new HashMap<String, Graph<Node, Edge>>();
		algorithmManager = am;
		randomGraphGenerator = rgg;
	}

	public void addData(Map<String, Graph<Node, Edge>> graphs) {
		testQueue.putAll(graphs);
	}

	public void runTest() {
		results = new ArrayList<Output>();
		for (String name : testQueue.keySet()) {
			// TODO te parametry zmienić
			results.addAll(algorithmManager.computeAll(name, testQueue.get(name), 0, 0));
		}
	}

	public ArrayList<Output> getResults() {
		return results;
	}

	public HashMap<String, Graph<Node, Edge>> getTestQueue() {
		return testQueue;
	}

	public void clearData() {
		testQueue = new HashMap<String, Graph<Node, Edge>>();
	}

	public void saveResults() {
		// TODO 
		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileOutputStream("test.txt"));
			for (Output o : results) {
				pw.println(o);
			}
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void generateArtificialData() {
		int maxNumberOfNodes = 100;
		double maxDensity = 50; // multiplied by 100

		for (int n = 10; n < maxNumberOfNodes; n += 10) {
			for (double d = 10; d < maxDensity; d += 10) {
				Properties properties = new Properties();
				properties.setProperty("nodes", String.valueOf(n));
				properties.setProperty("density", String.valueOf(d));
				testQueue.put("random " + properties.toString(), randomGraphGenerator.create(RandomGraphType.RANDOM, properties));
			}
		}

		int maxNumberOfCommunities = 4;
		double maxInsideDensity = 50; // multiplied by 100
		double maxTotalDensity = 30; // multiplied by 100
		for (int n = 10; n < maxNumberOfNodes; n += 10) {
			for (int c = 2; c < maxNumberOfCommunities; c++) {
				for (double totalD = 5; totalD < maxTotalDensity; totalD += 5) {
					for (double insideD = totalD + 5; insideD < maxInsideDensity; insideD += 10) {
						Properties properties = new Properties();
						properties.setProperty("nodes", String.valueOf(n));
						properties.setProperty("comm", String.valueOf(c));
						properties.setProperty("densityTotal", String.valueOf(totalD));
						properties.setProperty("densityInside", String.valueOf(insideD));
						testQueue.put("random modular " + properties.toString(), randomGraphGenerator.create(RandomGraphType.RANDOMMODULAR, properties));
					}
				}
			}

		}

		System.out.println("generated random graphs: " + testQueue.size());
	}

}
