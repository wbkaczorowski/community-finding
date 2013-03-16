package pl.edu.pw.elka.inz.community.finding.application.model;

import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Edge;
import pl.edu.pw.elka.inz.community.finding.application.model.graph.structure.Node;
import edu.uci.ics.jung.graph.Graph;

/**
 * Manager for community finding algorithms. Manages tests, chose correct parameters etc.
 * 
 * @author Wojciech Kaczorowski
 *
 */
public class AlgorithmManager {

	/**
	 * Type of algorithm actually use.
	 */
	private AlgorithmType algorithmType;
	
	private ProcessingMode processingMode;
		
	public AlgorithmManager() {
		// default algorithm
		this.algorithmType = AlgorithmType.GRIVAN_NEWMAN;
	}
	
	public void computeSingle() {
		long time = System.currentTimeMillis();		

		switch (algorithmType) {
		case LOUVAIN:
			break;
			
		case GRIVAN_NEWMAN:
			break;
			
		case WU_HUBERMAN:
			break;
			
		case CLAUSET_NEWMAN_MOORE:
			break;
			
		default:
			break;
		}
				
		long timeTotal = System.currentTimeMillis() - time;
		System.out.println("Czas obliczen: " + timeTotal + " ms");
	}	
	
	
	public AlgorithmType getAlgorithmType() {
		return algorithmType;
	}

	public void setAlgorithmType(AlgorithmType algorithmType) {
		this.algorithmType = algorithmType;
	}
	
	public ProcessingMode getProcessingMode() {
		return processingMode;
	}

	public void setProcessingMode(ProcessingMode processingMode) {
		this.processingMode = processingMode;
	}

	

	
}
