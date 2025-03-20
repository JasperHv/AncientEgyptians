package nl.vu.cs.softwaredesign.data.handlers;

import nl.vu.cs.softwaredesign.data.commands.Command;
import nl.vu.cs.softwaredesign.data.commands.GameSwipeCommand;
import nl.vu.cs.softwaredesign.data.commands.IntroSwipeCommand;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;

public class SwipeHandler {

    private final GameView gameView;

    public SwipeHandler(GameView gameView) {
        this.gameView = gameView;
    }

    public void onSwipe(SwipeSide side) {
        if (gameView.getIntroPhase()) {
            Command command = new IntroSwipeCommand(side, gameView.getIntroCards(), gameView);
            command.execute();
        } else {
            Command command = new GameSwipeCommand(
                    side,
                    gameView.getCurrentGameCard(),
                    gameView.getGameCardIndex(),
                    gameView.getScoreSettings(),
                    gameView.getYearCount(),
                    gameView,
                    gameView.getInfluencePillars()
            );
            command.execute();
        }
    }
}
