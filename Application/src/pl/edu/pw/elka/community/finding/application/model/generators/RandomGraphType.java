package pl.edu.pw.elka.community.finding.application.model.generators;


/**
 * Type for generated graphs, with human readable names.
 * @author Wojciech Kaczorowski
 *
 */
public enum RandomGraphType {
	RANDOM("random"),
	RANDOMMODULAR("random with communities"),
	ERDOSRENYI("Erdos-Renyi"),
	EPPSTEIN("Eppstein power law"),
	KLEINBERGSMALLWORLD("Kleinberg small worlds"),
	BARABASIALBERT("Barabasi Albert"),
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
