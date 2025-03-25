package nl.vu.cs.softwaredesign.data.commands;

import nl.vu.cs.softwaredesign.data.config.gamesettings.GameConfiguration;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ModeConfiguration;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import nl.vu.cs.softwaredesign.data.controller.GameStateController;

public class IntroSwipeCommand implements Command {
    private final SwipeSide side;
    private final GameStateController gameStateController;
    private final GameView gameView;

    public IntroSwipeCommand(SwipeSide side, GameStateController gameStateController, GameView gameView) {
        this.side = side;
        this.gameStateController = gameStateController;
        this.gameView = gameView;
    }

    @Override
    public void execute() {
        String currentCard = gameStateController.getIntroCards().get(gameStateController.getIntroCardIndex());

        if ("choose-pharaoh".equals(currentCard)) {
            GameConfiguration config = ModeConfiguration.getInstance().getGameConfig();
            String chosenPharaoh = (side == SwipeSide.LEFT) ? "Cleopatra" : "Tutankhamun";
            config.setSelectedCharacter(chosenPharaoh);
            ModeConfiguration.getInstance().updatePillarValues();

            gameStateController.setIntroCardIndex(gameStateController.getIntroCards().indexOf(chosenPharaoh.toLowerCase() + "-card"));
        }
        else if ("tutankhamun-card".equals(currentCard) || "cleopatra-card".equals(currentCard)) {
            gameStateController.setIntroPhase(false);
        }
        else {
            gameStateController.advanceIntroCard();
        }
    }
}
