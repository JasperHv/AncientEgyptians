package nl.vu.cs.softwaredesign.data.model;

import java.util.Map;

public class Monarch {
    private final String name;
    private final Map<String, Integer> initialValues;

    public Monarch(String name, Map<String, Integer> initialValues) {
        this.name = name;
        this.initialValues = initialValues;
    }

    public String getName() { return name; }

    public Map<String, Integer> getInitialValues() { return initialValues; }
}
