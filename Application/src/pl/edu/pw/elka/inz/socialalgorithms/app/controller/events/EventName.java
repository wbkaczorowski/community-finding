package pl.edu.pw.elka.inz.socialalgorithms.app.controller.events;

/**
 * Enumeracja zawierająca nazwy wszystkich zdarzeń jakie mogą być zgłaszane
 */
public enum EventName {

    /**
     * Event startowy, pokazuje puste okno.
     */
    SHOW_EMPTY_WINDOW,

    /**
     * Obsługa wczytania pliku.
     */
    CHOOSE_FILE,
    
    /**
     * Wylicz grupy dla danego algorytmu.
     */
    CALCULATE,

    /**
     * O programie.
     */
    ABOUT
}
