package pl.edu.pw.elka.inz.community.finding.application.model;

/**
 * Type for tested algorithms.
 * @author Wojciech Kaczorowski
 *
 */
public enum AlgorithmType {
	Louvain("Louvain"),
	GrivanNewman("Grivan-Newman"),
	WuHuberman("Wu-Huberman"),
	ClausetNewmanMoore("Clauset-Newman-Moore"),
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
