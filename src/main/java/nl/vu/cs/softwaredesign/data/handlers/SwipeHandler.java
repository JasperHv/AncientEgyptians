package nl.vu.cs.softwaredesign.data.handlers;

import nl.vu.cs.softwaredesign.data.commands.Command;
import nl.vu.cs.softwaredesign.data.commands.GameSwipeCommand;
import nl.vu.cs.softwaredesign.data.commands.IntroSwipeCommand;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;
import nl.vu.cs.softwaredesign.data.GameStateManager;

public class SwipeHandler {

    private final GameView gameView;
    private final GameStateManager gameStateManager;

    public SwipeHandler(GameView gameView, GameStateManager gameStateManager) {
        this.gameView = gameView;
        this.gameStateManager = gameStateManager;
    }

    public void onSwipe(SwipeSide side) {
        if (gameStateManager.isIntroPhase()) {
            Command command = new IntroSwipeCommand(side, gameStateManager, gameView);
            command.execute();
        } else {
            Command command = new GameSwipeCommand(
                    side,
                    gameStateManager.getCurrentGameCard(),
                    gameStateManager.getScoreSettings(),
                    gameStateManager.getYearCount(),
                    gameView,
                    gameStateManager,
                    gameStateManager.getInfluencePillars()
            );
            command.execute();
        }
    }
}
