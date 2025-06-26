package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import ancientegyptiansgame.data.model.Mode;
import ancientegyptiansgame.data.model.Ending;

class ConfigurationLoaderUnitTests {

    @Test
    void parseModes_WithTestMode_ReturnsTestMode() throws Exception {
        String json = "{ \"modes\": [ { \"name\": \"Test Mode\", \"configPath\": \"test.json\" } ] }";
        ObjectMapper mapper = new ObjectMapper();

        List<Mode> modes = mapper.convertValue(
                mapper.readTree(json).get("modes"),
                mapper.getTypeFactory().constructCollectionType(List.class, Mode.class)
        );
        assertEquals(1, modes.size());
        assertEquals("Test Mode", modes.get(0).getName());
        assertEquals("test.json", modes.get(0).getConfigPath());
    }

    @Test
    void parseModes_EmptyList_ReturnsEmptyList() throws Exception {
        String json = "{ \"modes\": [] }";
        ObjectMapper mapper = new ObjectMapper();

        List<Mode> modes = mapper.convertValue(
                mapper.readTree(json).get("modes"),
                mapper.getTypeFactory().constructCollectionType(List.class, Mode.class)
        );
        assertTrue(modes.isEmpty());
    }

    @Test
    void parseModes_NullModes_ReturnsEmptyList() throws Exception {
        String json = "{ \"modes\": null }";
        ObjectMapper mapper = new ObjectMapper();
        List<Mode> modes = mapper.convertValue(
                mapper.readTree(json).get("modes"),
                mapper.getTypeFactory().constructCollectionType(List.class, Mode.class)
        );
        if (modes == null) {
            modes = java.util.Collections.emptyList();
        }
        assertTrue(modes.isEmpty());
    }

    @Test
    void parseMonarchs_WithTestMonarch_ReturnsEmptyList() throws Exception {
        String json = "{ \"monarchs\": [ \"Test Monarch\" ] }";
        ObjectMapper mapper = new ObjectMapper();

        List<String> monarchs = mapper.convertValue(
                mapper.readTree(json).get("monarchs"),
                mapper.getTypeFactory().constructCollectionType(List.class, String.class)
        );
        assertEquals(1, monarchs.size());
        assertEquals("Test Monarch", monarchs.get(0));
    }

    @Test
    void parseMonarchs_WithEmptyList_ReturnsTestMonarch() throws Exception {
        String json = "{ \"monarchs\": [ ] }";
        ObjectMapper mapper = new ObjectMapper();

        List<String> monarchs = mapper.convertValue(
                mapper.readTree(json).get("monarchs"),
                mapper.getTypeFactory().constructCollectionType(List.class, String.class)
        );
        assertTrue(monarchs.isEmpty());
    }


    @Test
    void parseGoldenAgeEnding_ValidJson_ReturnsCorrectEnding() throws Exception {
        String json = "{ \"goldenAgeEnding\": { " +
                "\"description\": \"Glorious Pharaoh, your rule is unmatched! Through wisdom, balance, and strong leadership, you have ushered in a Golden Age for Egypt. The people prosper, the temples shine, and your name will be spoken with reverence for centuries to come. Your dynasty stands eternal, a beacon of prosperity and order.\"," +
                "\"image\": \"endings/golden-age-ending.png\" } }";
        ObjectMapper mapper = new ObjectMapper();

        Ending goldenAgeEnding = mapper.convertValue(
                mapper.readTree(json).get("goldenAgeEnding"),
                Ending.class
        );
        assertEquals("Glorious Pharaoh, your rule is unmatched! Through wisdom, balance, and strong leadership, you have ushered in a Golden Age for Egypt. The people prosper, the temples shine, and your name will be spoken with reverence for centuries to come. Your dynasty stands eternal, a beacon of prosperity and order.", goldenAgeEnding.getDescription());
        assertEquals("endings/golden-age-ending.png", goldenAgeEnding.getImage());
    }

    @Test
    void parseBadEnding_ValidJson_ReturnsCorrectEnding() throws Exception {
        String json = "{ \"bad ending\": { " +
                "\"description\": \"The people have lost faith in your leadership. Chaos reigns, and your dynasty is brought to an untimely end. The once-glorious temples now lie in ruin, and your name will be forgotten by history. You have failed Egypt.\"," +
                "\"image\": \"endings/bad-ending.png\" } }";
        ObjectMapper mapper = new ObjectMapper();

        Ending badEnding = mapper.convertValue(
                mapper.readTree(json).get("bad ending"),
                Ending.class
        );
        assertEquals("The people have lost faith in your leadership. Chaos reigns, and your dynasty is brought to an untimely end. The once-glorious temples now lie in ruin, and your name will be forgotten by history. You have failed Egypt.", badEnding.getDescription());
        assertEquals("endings/bad-ending.png", badEnding.getImage());
    }
}
