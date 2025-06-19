package ancientegyptiansgame.logging;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IntroCommandLogEntry extends CommandLogEntry {
    @JsonProperty("chosenCharacter")
    private final String chosenCharacter;

    public IntroCommandLogEntry(String cardTitle, String swipeDirection, String chosenCharacter, long timestamp) {
        super("IntroSwipeCommand", cardTitle, swipeDirection, timestamp);
        this.chosenCharacter = chosenCharacter;
    }

    public String getChosenCharacter() {
        return chosenCharacter;
    }
}
