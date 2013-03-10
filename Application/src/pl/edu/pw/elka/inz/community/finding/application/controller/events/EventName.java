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
    CHOOSE_FILE,
    
    /**
     * Calculate for given algorithm.
     */
    CALCULATE,

    /**
     * About.
     */
    ABOUT
}
