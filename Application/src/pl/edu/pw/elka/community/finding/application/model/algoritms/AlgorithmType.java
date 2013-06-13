package pl.edu.pw.elka.community.finding.application.model.algoritms;

/**
 * Type for tested algorithms, with human readable names.
 * @author Wojciech Kaczorowski
 *
 */
public enum AlgorithmType {
	LOUVAIN("Louvain"),
	GRIVAN_NEWMAN("Grivan-Newman"),
	WU_HUBERMAN("Wu-Huberman"),
	FAST_NEWMAM("Fast Newman Algorithm"),
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
