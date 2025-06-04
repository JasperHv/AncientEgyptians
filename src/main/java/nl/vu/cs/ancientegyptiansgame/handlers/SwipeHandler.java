package nl.vu.cs.ancientegyptiansgame.handlers;

import nl.vu.cs.ancientegyptiansgame.commands.Command;
import nl.vu.cs.ancientegyptiansgame.commands.GameSwipeCommand;
import nl.vu.cs.ancientegyptiansgame.commands.IntroSwipeCommand;
import nl.vu.cs.ancientegyptiansgame.data.enums.SwipeSide;
import nl.vu.cs.ancientegyptiansgame.controller.GameStateController;
import nl.vu.cs.ancientegyptiansgame.listeners.EndingListener;
import nl.vu.cs.ancientegyptiansgame.ui.views.GameView;

public class SwipeHandler {

    private final GameStateController gameStateController;
    private final EndingListener endingListener;

    public SwipeHandler(GameStateController gameStateController, EndingListener endingListener) {
        this.gameStateController = gameStateController;
        this.endingListener = endingListener;
    }

    public void onSwipe(SwipeSide side) {
        Command command;
        if (gameStateController.isIntroPhase()) {
            command = new IntroSwipeCommand(side, gameStateController);
        } else {
            command = new GameSwipeCommand(side, gameStateController, endingListener);
        }
        command.execute();
    }
}
