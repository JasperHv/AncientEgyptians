package nl.vu.cs.softwaredesign.exception;

public class ConfigurationNotFoundException extends RuntimeException {
    public ConfigurationNotFoundException() {
        super("Configuration not found!");
    }
}
