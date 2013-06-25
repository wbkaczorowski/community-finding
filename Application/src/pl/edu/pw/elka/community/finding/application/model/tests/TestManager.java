package pl.edu.pw.elka.community.finding.application.model.tests;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections15.CollectionUtils;

import pl.edu.pw.elka.community.finding.application.model.algoritms.AlgorithmManager;
import pl.edu.pw.elka.community.finding.application.model.generators.RandomGraphGenerator;
import pl.edu.pw.elka.community.finding.application.model.generators.RandomGraphType;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.graph.Graph;


public class TestManager {

	private HashMap<Properties, Graph<Node, Edge>> testQueue;
	private AlgorithmManager algorithmManager;
	private RandomGraphGenerator randomGraphGenerator;
	private ArrayList<Output> results;
	private HashMap<Properties, HashMap<String, Double>> comparedPartitions;

	public TestManager(AlgorithmManager am, RandomGraphGenerator rgg) {
		testQueue = new HashMap<Properties, Graph<Node, Edge>>();
		algorithmManager = am;
		randomGraphGenerator = rgg;
		comparedPartitions = new HashMap<>();
	}

	public void addData(Map<Properties, Graph<Node, Edge>> graphs) {
		testQueue.putAll(graphs);
	}

	public void runTest() {
		results = new ArrayList<Output>();
		for (Properties property : testQueue.keySet()) {
			System.out.println(property);
		}
		for (Properties property : testQueue.keySet()) {
			Collection<Output> testedGragh = algorithmManager.computeAll(property, testQueue.get(property));
			comparedPartitions.put(property, partitionComparator(testedGragh));
			// System.out.println(partitionComparator(testedGragh));
			results.addAll(testedGragh);
		}
	}

	private HashMap<String, Double> partitionComparator(Collection<Output> testedGraph) {
		HashMap<String, Double> cp = new HashMap<String, Double>();
		for (Output o1 : testedGraph) {
			for (Output o2 : testedGraph) {
				if (!o1.equals(o2)) {
					double similarity = 0;
					for (Set<Node> g1 : o1.getCommunities()) {
						for (Set<Node> g2 : o2.getCommunities()) {
							similarity += (double) CollectionUtils.intersection(g1, g2).size() / (double) CollectionUtils.union(g1, g2).size();
						}
					}
					similarity /= (double) (o1.getCommunities().size() + o2.getCommunities().size()) / 2.0;
					cp.put(o1.getProperties().getProperty("algorithmType") + " " + o2.getProperties().getProperty("algorithmType"), similarity);
				}
			}
		}
		return cp;
	}
	
	private void averageSimilarity() {
		HashMap<String, Double> averageSimilarity = new HashMap<>();
		for (HashMap<String, Double> values : comparedPartitions.values()) {
			for (String pair : values.keySet()) {
				if (averageSimilarity.get(pair) != null) {
					averageSimilarity.put(pair, averageSimilarity.get(pair) + values.get(pair));
				} else {
					averageSimilarity.put(pair, values.get(pair));
				}
			}
		}
		for (String s : averageSimilarity.keySet()) {
			averageSimilarity.put(s, averageSimilarity.get(s) / (double) comparedPartitions.size());
			System.out.println(s + " " + averageSimilarity.get(s));
		}
	}

	public ArrayList<Output> getResults() {
		return results;
	}

	public HashMap<Properties, Graph<Node, Edge>> getTestQueue() {
		return testQueue;
	}

	public void clearData() {
		testQueue = new HashMap<Properties, Graph<Node, Edge>>();
	}

	public void saveResults() {
		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileOutputStream("test.csv"));
			for (Output o : results) {
				pw.println(o.toCSVline());
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			//TODO na csv zmiennić
			pw = new PrintWriter(new FileOutputStream("partitionCompare.txt"));
			for (Properties p : comparedPartitions.keySet()) {
				pw.println(p + " " + comparedPartitions.get(p));
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		averageSimilarity();
	}
	
	public void generateArtificialData() {
		int maxNumberOfNodes = 200;
		double maxDensity = 30; // divided by 100

		for (int n = 20; n <= maxNumberOfNodes; n += 20) {
			for (double d = 5; d <= maxDensity; d += 5) {
				Properties properties = new Properties();
				properties.setProperty("graphType", "random");
				properties.setProperty("nodes", String.valueOf(n));
				properties.setProperty("density", String.valueOf(d));
				testQueue.put(properties, randomGraphGenerator.create(RandomGraphType.RANDOM, properties));
			}
		}

		int maxNumberOfCommunities = 5;
		double maxInsideDensity = 55; // multiplied by 100
		double maxTotalDensity = 30; // multiplied by 100
		for (int n = 20; n <= maxNumberOfNodes; n += 20) {
			for (int c = 2; c <= maxNumberOfCommunities; ++c) {
				for (double totalD = 5; totalD < maxTotalDensity; totalD += 5) {
					for (double insideD = totalD + 5; insideD < maxInsideDensity; insideD += 10) {
						Properties properties = new Properties();
						properties.setProperty("graphType", "random modular");
						properties.setProperty("nodes", String.valueOf(n));
						properties.setProperty("comm", String.valueOf(c));
						properties.setProperty("densityTotal", String.valueOf(totalD));
						properties.setProperty("densityInside", String.valueOf(insideD));
						testQueue.put(properties, randomGraphGenerator.create(RandomGraphType.RANDOMMODULAR, properties));
					}
				}
			}
		}
		System.out.println("generated random graphs: " + testQueue.size());
	}

}
