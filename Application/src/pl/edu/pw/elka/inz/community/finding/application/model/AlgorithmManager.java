package pl.edu.pw.elka.inz.community.finding.application.model;

/**
 * 
 * 
 * @author Wojciech Kaczorowski
 *
 */
public class AlgorithmManager {

	private AlgorithmType algorithmType;
	
	public AlgorithmManager() {
		// default algorithm
		this.algorithmType = AlgorithmType.GrivanNewman;
	}

	
	
	
		
	public AlgorithmType getAlgorithmType() {
		return algorithmType;
	}

	public void setAlgorithmType(AlgorithmType algorithmType) {
		this.algorithmType = algorithmType;
	}
	
}
