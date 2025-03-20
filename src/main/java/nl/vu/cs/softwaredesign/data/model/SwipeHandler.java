package nl.vu.cs.softwaredesign.data.model;

public class SwipeHandler {

    public void onSwipe(SwipeSide side, Card currentCard) {
        Command command = new SwipeCommand(currentCard, side);
        command.execute();
    }
}
