package nl.vu.cs.softwaredesign.data.logging;

public class CommandLogEntry {
    private final String commandType;
    private final String cardTitle;
    private final String swipeDirection;
    private final int rawInfluenceValue; // For commands that affect influence (default 0 if not applicable)
    private final long timestamp;

    public CommandLogEntry(String commandType, String cardTitle, String swipeDirection, int rawInfluenceValue, long timestamp) {
        this.commandType = commandType;
        this.cardTitle = cardTitle;
        this.swipeDirection = swipeDirection;
        this.rawInfluenceValue = rawInfluenceValue;
        this.timestamp = timestamp;
    }

    // Getters and setters if needed

    public String getCommandType() {
        return commandType;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public String getSwipeDirection() {
        return swipeDirection;
    }

    public int getRawInfluenceValue() {
        return rawInfluenceValue;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
