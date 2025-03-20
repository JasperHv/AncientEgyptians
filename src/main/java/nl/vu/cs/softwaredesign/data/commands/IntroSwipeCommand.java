package nl.vu.cs.softwaredesign.data.commands;

import nl.vu.cs.softwaredesign.data.config.gamesettings.GameConfiguration;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ModeConfiguration;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;
import nl.vu.cs.softwaredesign.ui.views.GameView;

import java.util.List;

public class IntroSwipeCommand implements Command {
    private final SwipeSide side;
    private final List<String> introCards;
    private final GameView gameView;

    public IntroSwipeCommand(SwipeSide side, List<String> introCards, GameView gameView) {
        this.side = side;
        this.introCards = introCards;
        this.gameView = gameView;
    }

    @Override
    public void execute() {
        int introCardIndex = gameView.getIntroCardIndex(); // Get current index from GameView
        System.out.println("introCardIndex before: " + introCardIndex);
        String currentCard = introCards.get(introCardIndex);

        if ("choose-pharaoh".equals(currentCard)) {
            GameConfiguration config = ModeConfiguration.getInstance().getGameConfig();
            if (side == SwipeSide.LEFT) {
                config.setSelectedCharacter("Cleopatra");
            } else {
                config.setSelectedCharacter("Tutankhamun");
            }
            introCardIndex = introCards.indexOf(side == SwipeSide.LEFT ? "cleopatra-card" : "tutankhamun-card");
            ModeConfiguration.getInstance().updatePillarValues();
        } else if ("tutankhamun-card".equals(currentCard) || "cleopatra-card".equals(currentCard)) {
            GameView.setIntroPhase(false);
        } else {
            introCardIndex++;
        }

        gameView.setIntroCardIndex(introCardIndex);  // Update GameView
        System.out.println("introCardIndex after: " + introCardIndex);
    }
}
