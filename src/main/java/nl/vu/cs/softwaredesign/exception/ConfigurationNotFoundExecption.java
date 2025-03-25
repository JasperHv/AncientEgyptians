package nl.vu.cs.softwaredesign.exception;

public class ConfigurationNotFoundExecption extends RuntimeException {
    public ConfigurationNotFoundExecption(String message) {
        super(message);
    }

    public ConfigurationNotFoundExecption(String message, Throwable cause) {
        super(message, cause);
    }
}
