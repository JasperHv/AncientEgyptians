package nl.vu.cs.softwaredesign.data.logging;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class CommandLogEntry {
    @JsonProperty("commandType")
    private final String commandType;

    @JsonProperty("cardTitle")
    private final String cardTitle;

    @JsonProperty("swipeDirection")
    private final String swipeDirection;

    @JsonProperty("timestamp")
    private final long timestamp;

    protected CommandLogEntry(String commandType, String cardTitle, String swipeDirection, long timestamp) {
        this.commandType = commandType;
        this.cardTitle = cardTitle;
        this.swipeDirection = swipeDirection;
        this.timestamp = timestamp;
    }

    public String getCommandType() {
        return commandType;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public String getSwipeDirection() {
        return swipeDirection;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
