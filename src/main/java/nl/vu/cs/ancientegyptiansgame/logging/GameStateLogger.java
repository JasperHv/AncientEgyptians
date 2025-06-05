package nl.vu.cs.ancientegyptiansgame.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameStateLogger {
    private static final String LOG_FILE = "src/main/resources/configuration/gamestate.json";
    private static final List<GameStateEntry> logEntries = new ArrayList<>();
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
    private static final Logger logger = Logger.getLogger(GameStateLogger.class.getName());

    private GameStateLogger() {}

    public static void logGameState(GameStateEntry entry) {
        logEntries.add(entry);
        saveLogToFile();
    }

    private static void saveLogToFile() {
        try {
            mapper.writeValue(new File(LOG_FILE), logEntries);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving gamestate log to file", e);
        }
    }
}

