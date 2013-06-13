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
    MULTI_TESTS_REAL,
    
    /**
     * Calculate single graph groups with visualization.
     */
    CALCULATE_SINGLE,
    
    /**
     * Make multiple tests.
     */
    MULTI_TESTS,
    
    /**
     * Make multiple tests with artificial data.
     */
    MULTI_TESTS_ARTIFICIAL,

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
    
    /**
     * Generates random graph.
     */
    GENERATE_RANODM,

}
