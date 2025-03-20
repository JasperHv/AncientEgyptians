package nl.vu.cs.softwaredesign.data.handlers;

import nl.vu.cs.softwaredesign.data.commands.Command;
import nl.vu.cs.softwaredesign.data.commands.GameSwipeCommand;
import nl.vu.cs.softwaredesign.data.commands.IntroSwipeCommand;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;
import nl.vu.cs.softwaredesign.data.controller.GameStateController;

public class SwipeHandler {

    private final GameView gameView;
    private final GameStateController gameStateController;

    public SwipeHandler(GameView gameView, GameStateController gameStateController) {
        this.gameView = gameView;
        this.gameStateController = gameStateController;
    }

    public void onSwipe(SwipeSide side) {
        if (gameStateController.isIntroPhase()) {
            Command command = new IntroSwipeCommand(side, gameStateController, gameView);
            command.execute();
        } else {
            Command command = new GameSwipeCommand(
                    side,
                    gameStateController.getCurrentGameCard(),
                    gameStateController.getScoreSettings(),
                    gameStateController.getYearCount(),
                    gameView,
                    gameStateController,
                    gameStateController.getInfluencePillars()
            );
            command.execute();
        }
    }
}
