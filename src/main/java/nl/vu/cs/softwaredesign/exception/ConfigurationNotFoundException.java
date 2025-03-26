package nl.vu.cs.softwaredesign.exception;

public class ConfigurationNotFoundException extends RuntimeException {
    public ConfigurationNotFoundException(String message) {
        super(message);
    }

    public ConfigurationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
