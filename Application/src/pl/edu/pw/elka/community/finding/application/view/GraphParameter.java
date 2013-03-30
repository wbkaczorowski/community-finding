package pl.edu.pw.elka.community.finding.application.view;

public class GraphParameter {
	
	private int egdesNumber;
	
	private int nodesNumber;
	
	public GraphParameter() {
		
	}

	public GraphParameter(int egdesNumber, int nodesNumber) {
		super();
		this.egdesNumber = egdesNumber;
		this.nodesNumber = nodesNumber;
	}

	public int getEgdesNumber() {
		return egdesNumber;
	}

	public void setEgdesNumber(int egdesNumber) {
		this.egdesNumber = egdesNumber;
	}

	public int getNodesNumber() {
		return nodesNumber;
	}

	public void setNodesNumber(int nodesNumber) {
		this.nodesNumber = nodesNumber;
	}
	
}
