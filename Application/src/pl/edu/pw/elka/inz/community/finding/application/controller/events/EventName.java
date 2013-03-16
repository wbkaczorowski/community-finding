package pl.edu.pw.elka.inz.community.finding.application.controller.events;

/**
 * Enumerate type, containing names for all events.
 */
public enum EventName {

    /**
     * Starting event, shows empty window.
     */
    START,

    /**
     * Service for loading file with data.
     */
    OPEN_FILE,
    
    /**
     * Load directory with graphs.
     */
    OPEN_DIRECTORY,
    
    /**
     * Calculate groups with visualization.
     */
    CALCULATE,
    
    /**
     * Calculate groups without visualization.
     */
    CALCULATE_MULTI,

    /**
     * About.
     */
    ABOUT
}
