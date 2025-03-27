package nl.vu.cs.ancientegyptiansgame.handlers;

import nl.vu.cs.ancientegyptiansgame.commands.Command;
import nl.vu.cs.ancientegyptiansgame.commands.GameSwipeCommand;
import nl.vu.cs.ancientegyptiansgame.commands.IntroSwipeCommand;
import nl.vu.cs.ancientegyptiansgame.data.enums.SwipeSide;
import nl.vu.cs.ancientegyptiansgame.controller.GameStateController;
public class SwipeHandler {

    private final GameStateController gameStateController;

    public SwipeHandler(GameStateController gameStateController) {
        this.gameStateController = gameStateController;
    }

    public void onSwipe(SwipeSide side) {
        Command command;
        if (gameStateController.isIntroPhase()) {
            command = new IntroSwipeCommand(side, gameStateController);
        } else {
            command = new GameSwipeCommand(
                    side,
                    gameStateController
            );
        }
        command.execute();
    }
}
