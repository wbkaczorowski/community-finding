package pl.edu.pw.elka.inz.community.finding.application.model;

/**
 * Type for tested algorithms, with human readable names.
 * @author Wojciech Kaczorowski
 *
 */
public enum AlgorithmType {
	LOUVAIN("Louvain"),
	GRIVAN_NEWMAN("Grivan-Newman"),
	WU_HUBERMAN("Wu-Huberman"),
	CLAUSET_NEWMAN_MOORE("Clauset-Newman-Moore"),
	;
	
	private final String text;
	
	private AlgorithmType(String name) {
		this.text = name;
	}
	
    @Override
    public String toString() {
        return text;
    }
	
}
