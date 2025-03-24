package nl.vu.cs.softwaredesign.data.commands;

import nl.vu.cs.softwaredesign.data.config.gamesettings.GameConfiguration;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ModeConfiguration;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;
import nl.vu.cs.softwaredesign.data.logging.IntroCommandLogEntry;
import nl.vu.cs.softwaredesign.data.logging.CommandLogger;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import nl.vu.cs.softwaredesign.data.controller.GameStateController;
import nl.vu.cs.softwaredesign.data.model.Monarch;

public class IntroSwipeCommand implements Command {
    private final SwipeSide side;
    private final GameStateController gameStateController;
    public IntroSwipeCommand(SwipeSide side, GameStateController gameStateController, GameView gameView) {
        this.side = side;
        this.gameStateController = gameStateController;
    }

    @Override
    public void execute() {
        String currentCard = gameStateController.getIntroCards().get(gameStateController.getIntroCardIndex());
        Monarch chosenMonarch = null;

        if ("choose-pharaoh".equals(currentCard)) {
            GameConfiguration config = ModeConfiguration.getInstance().getGameConfig();
            String monarchName = (side == SwipeSide.LEFT) ? "Cleopatra" : "Tutankhamun";

            chosenMonarch = new Monarch(monarchName, config.getMonarchInitialValues().get(monarchName));
            config.setSelectedMonarch(monarchName);
            ModeConfiguration.getInstance().updatePillarValues();

            gameStateController.setIntroCardIndex(gameStateController.getIntroCards().indexOf(monarchName.toLowerCase() + "-card"));

            IntroCommandLogEntry entry = new IntroCommandLogEntry(
                    currentCard,
                    side.toString(),
                    monarchName,
                    System.currentTimeMillis()
            );

            CommandLogger.logCommand(entry);
        } else if ("tutankhamun-card".equals(currentCard) || "cleopatra-card".equals(currentCard)) {
            gameStateController.setIntroPhase(false);
        } else {
            gameStateController.advanceIntroCard();
        }
    }
}
