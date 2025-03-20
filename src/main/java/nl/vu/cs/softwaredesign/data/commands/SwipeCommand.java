package nl.vu.cs.softwaredesign.data.commands;

import nl.vu.cs.softwaredesign.data.enums.SwipeSide;
import nl.vu.cs.softwaredesign.data.model.Card;

public class SwipeCommand implements Command {
    private final Card card;
    private final SwipeSide side;

    public SwipeCommand(Card card, SwipeSide side) {
        this.card = card;
        this.side = side;
    }

    @Override
    public void execute() {

    }
}
