package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import ancientegyptiansgame.data.model.Mode;

public class ConfigurationLoaderUnitTests {

    @Test
    public void parseModes_WithTestMode_ReturnsTestMode() throws Exception {
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
}
