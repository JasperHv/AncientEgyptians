package ancientegyptiansgame.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandLogger {
    private static final String LOG_FILE = "src/main/resources/configuration/command_log.json";
    private static final List<CommandLogEntry> logEntries = new ArrayList<>();
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
    private static final Logger logger = Logger.getLogger(CommandLogger.class.getName());

    private CommandLogger() {}

    public static void logCommand(CommandLogEntry entry) {
        logEntries.add(entry);
        saveLogToFile();
    }

    private static void saveLogToFile() {
        try {
            mapper.writeValue(new File(LOG_FILE), logEntries);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving command log to file", e);
        }
    }

    public static void clearLogFile() {
        try {
            logEntries.clear();
            mapper.writeValue(new File(LOG_FILE), logEntries);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to clear command log file", e);
        }
    }

    public static List<CommandLogEntry> getLogEntries() {
        return new ArrayList<>(logEntries);
    }
}

