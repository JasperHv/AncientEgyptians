package ancientegyptiansgame.handlers;

import ancientegyptiansgame.commands.Command;
import ancientegyptiansgame.commands.GameSwipeCommand;
import ancientegyptiansgame.commands.IntroSwipeCommand;
import ancientegyptiansgame.data.enums.SwipeSide;
import ancientegyptiansgame.controller.GameStateController;
import ancientegyptiansgame.listeners.EndingListener;
import ancientegyptiansgame.ui.views.GameView;

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
