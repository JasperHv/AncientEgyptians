package ancientegyptiansgame.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static List<GameStateEntry> loadGameStates() {
        try (InputStream input = GameStateLogger.class.getResourceAsStream("/configuration/gamestate.json")) {
            if (input == null) {
                logger.log(Level.WARNING, "Gamestate file not found at /configuration/gamestate.json");
                return new ArrayList<>();
            }

            ObjectMapper loadMapper = new ObjectMapper();
            GameStateEntry[] gameStates = loadMapper.readValue(input, GameStateEntry[].class);
            return Arrays.asList(gameStates);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading gamestate from file", e);
            return new ArrayList<>();
        }
    }
}

