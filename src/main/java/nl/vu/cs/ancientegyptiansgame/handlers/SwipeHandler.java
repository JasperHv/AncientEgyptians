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

    /**
     * Constructs a SwipeHandler to manage swipe events using the provided game state controller and ending listener.
     *
     * @param gameStateController the controller managing the current game state
     * @param endingListener the listener to be notified when the game ends
     */
    public SwipeHandler(GameStateController gameStateController, EndingListener endingListener) {
        this.gameStateController = gameStateController;
        this.endingListener = endingListener;
    }

    /**
     * Handles a swipe event by executing the appropriate command based on the current game phase.
     *
     * @param side the direction of the swipe input
     */
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
