package pl.edu.pw.elka.community.finding.application.controller.events;

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
     * Calculate single graph groups with visualization.
     */
    CALCULATE_SINGLE,
    
    /**
     * Calculate groups without visualization.
     */
    CALCULATE_MULTI,

    /**
     * About.
     */
    ABOUT,
    
    /**
     * Custom event for easy develop and debugging.
     */
    DEBUG,
    
    /**
     * Refreshes view.
     */
    REFRESH_VIEW,

}
