package nl.vu.cs.softwaredesign.data.handlers;

import nl.vu.cs.softwaredesign.data.commands.Command;
import nl.vu.cs.softwaredesign.data.commands.GameSwipeCommand;
import nl.vu.cs.softwaredesign.data.commands.IntroSwipeCommand;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;
import nl.vu.cs.softwaredesign.data.controller.GameStateController;
import nl.vu.cs.softwaredesign.data.config.gamesettings.GameConfiguration;

public class SwipeHandler {

    private final GameView gameView;
    private final GameStateController gameStateController;
    private final GameConfiguration gameConfiguration;

    public SwipeHandler(GameView gameView, GameStateController gameStateController, GameConfiguration gameConfiguration) {
        this.gameView = gameView;
        this.gameStateController = gameStateController;
        this.gameConfiguration = gameConfiguration;  // Initialize GameConfiguration
    }

    public void onSwipe(SwipeSide side) {
        Command command;
        if (gameStateController.isIntroPhase()) {
            command = new IntroSwipeCommand(side, gameStateController);
        } else {
            command = new GameSwipeCommand(
                    side,
                    gameStateController.getCurrentGameCard(),
                    gameStateController.getScoreSettings(),
                    gameView,
                    gameStateController,
                    gameStateController.getInfluencePillars(),
                    gameConfiguration
            );
        }
        command.execute();
    }
}
