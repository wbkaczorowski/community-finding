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
     * Calculate groups with visualization.
     */
    CALCULATE,
    
    /**
     * Calculate groups without visualization.
     */
    CALCULATE_NO_VIEW,

    /**
     * About.
     */
    ABOUT
}
