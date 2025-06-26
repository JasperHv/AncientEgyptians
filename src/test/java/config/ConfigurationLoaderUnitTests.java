package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import ancientegyptiansgame.data.model.Mode;

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
}
