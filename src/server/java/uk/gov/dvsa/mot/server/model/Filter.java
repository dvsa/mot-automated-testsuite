package uk.gov.dvsa.mot.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates filtering cached datasets so that the entries shared between tests are unique.
 */
public class Filter {

    /** The filter name. */
    private final String name;

    /** The unique values. */
    private final List<String> values;

    /**
     * Creates a new instance.
     * @param name  The name
     */
    public Filter(String name) {
        this.name = name;
        this.values = new ArrayList<>();
    }

    /**
     * Get the name.
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Determines whether the value is unique.
     * @param value     The value to check
     * @return <code>true</code> if unique
     */
    public boolean isDataUnique(String value) {
        return !values.contains(value);
    }

    /**
     * Adds the value to the filter.
     * @param value     The value to add
     */
    public void addToFilter(String value) {
        values.add(value);
    }
}
