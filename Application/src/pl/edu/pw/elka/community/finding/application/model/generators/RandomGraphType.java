package pl.edu.pw.elka.community.finding.application.model.generators;

/**
 * Type for tested algorithms, with human readable names.
 * @author Wojciech Kaczorowski
 *
 */
public enum RandomGraphType {
	RADNDOM("Random"),
	ERDOSRENYI("Erdos-Renyi"),
	TEST("Test"),
	;
	
	private final String text;
	
	private RandomGraphType(String name) {
		this.text = name;
	}
	
    @Override
    public String toString() {
        return text;
    }
	
}
