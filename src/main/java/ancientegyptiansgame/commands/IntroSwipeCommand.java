package ancientegyptiansgame.commands;

import ancientegyptiansgame.config.gamesettings.GameConfiguration;
import ancientegyptiansgame.config.gamesettings.ModeConfiguration;
import ancientegyptiansgame.data.enums.SwipeSide;
import ancientegyptiansgame.logging.IntroCommandLogEntry;
import ancientegyptiansgame.logging.CommandLogger;
import ancientegyptiansgame.controller.GameStateController;

public class IntroSwipeCommand implements Command {
    private final SwipeSide side;
    private final GameStateController gameStateController;

    public IntroSwipeCommand(SwipeSide side, GameStateController gameStateController) {
        this.side = side;
        this.gameStateController = gameStateController;
    }

    @Override
    public void execute() {
        String currentCard = gameStateController.getCurrentIntroCard();

        if ("choose-pharaoh".equals(currentCard)) {
            GameConfiguration gameConfig = GameConfiguration.getInstance();

            String monarchName = (side == SwipeSide.LEFT) ? "Cleopatra" : "Tutankhamun";
            gameConfig.setSelectedMonarch(monarchName);
            ModeConfiguration.getInstance().updatePillarValues();

            int nextIndex = gameStateController.getIntroCards().indexOf(monarchName.toLowerCase() + "-card");
            gameStateController.setIntroCardIndex(nextIndex);

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
