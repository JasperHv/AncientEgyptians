package nl.vu.cs.softwaredesign.data.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandLogger {
    private static final String LOG_FILE = "src/main/resources/configuration/command_log.json";
    private static final List<CommandLogEntry> logEntries = new ArrayList<>();
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);

    public static void logCommand(CommandLogEntry entry) {
        logEntries.add(entry);
        saveLogToFile();
    }

    private static void saveLogToFile() {
        try {
            mapper.writeValue(new File(LOG_FILE), logEntries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

