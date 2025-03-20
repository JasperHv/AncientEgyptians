package nl.vu.cs.softwaredesign.data.commands;

import nl.vu.cs.softwaredesign.data.config.gamesettings.GameConfiguration;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ModeConfiguration;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import nl.vu.cs.softwaredesign.data.GameStateManager;

import java.util.List;

public class IntroSwipeCommand implements Command {
    private final SwipeSide side;
    private final GameStateManager gameStateManager;
    private final GameView gameView;

    public IntroSwipeCommand(SwipeSide side, GameStateManager gameStateManager, GameView gameView) {
        this.side = side;
        this.gameStateManager = gameStateManager;
        this.gameView = gameView;
    }

    @Override
    public void execute() {
        String currentCard = gameStateManager.getIntroCards().get(gameStateManager.getIntroCardIndex());

        if ("choose-pharaoh".equals(currentCard)) {
            GameConfiguration config = ModeConfiguration.getInstance().getGameConfig();
            String chosenPharaoh = (side == SwipeSide.LEFT) ? "Cleopatra" : "Tutankhamun";
            config.setSelectedCharacter(chosenPharaoh);
            ModeConfiguration.getInstance().updatePillarValues();

            gameStateManager.setIntroCardIndex(gameStateManager.getIntroCards().indexOf(chosenPharaoh.toLowerCase() + "-card"));
        }
        else if ("tutankhamun-card".equals(currentCard) || "cleopatra-card".equals(currentCard)) {
            gameStateManager.setIntroPhase(false);
        }
        else {
            gameStateManager.advanceIntroCard();
        }
    }
}
