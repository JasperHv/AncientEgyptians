package ancientegyptiansgame.logging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IntroCommandLogEntry extends CommandLogEntry {
    private final String chosenCharacter;

    @JsonCreator
    public IntroCommandLogEntry(
            @JsonProperty("cardTitle") String cardTitle,
            @JsonProperty("swipeDirection") String swipeDirection,
            @JsonProperty("chosenCharacter") String chosenCharacter,
            @JsonProperty("timestamp") long timestamp
    ) {
        super("IntroSwipeCommand", cardTitle, swipeDirection, timestamp);
        this.chosenCharacter = chosenCharacter;
    }

    public String getChosenCharacter() {
        return chosenCharacter;
    }
}