package ancientegyptiansgame.handlers;

import ancientegyptiansgame.config.gamesettings.ModeConfiguration;
import ancientegyptiansgame.data.enums.SwipeSide;
import ancientegyptiansgame.data.model.Influence;
import ancientegyptiansgame.data.model.Pillars;
import ancientegyptiansgame.data.model.PillarData;

import java.util.List;
import java.util.stream.Collectors;

public class HandleInfluencePillars {
    private final ModeConfiguration modeConfiguration = ModeConfiguration.getInstance();


    public void applyInfluence(SwipeSide side, List<Influence> influences) {
        if (influences == null || influences.isEmpty()) return;
        boolean isSwipeLeft = side == SwipeSide.LEFT;

        // Adjust influences based on the swipe direction
        List<Influence> adjustedInfluences = influences.stream()
                .map(influence -> new Influence(
                        influence.getPillar(),
                        isSwipeLeft ? -influence.getValue() : influence.getValue()
                ))
                .collect(Collectors.toList());

        for (Influence influence : adjustedInfluences) {
            Pillars pillarsEnum = Pillars.fromName(influence.getPillar());
            int valueChange = influence.getValue();

            PillarData pillarData = modeConfiguration.getPillarData(pillarsEnum);
            int currentValue = pillarData.getValue();
            int newValue = Math.min(Math.max(currentValue + valueChange, 0), 100);

            pillarData.setValue(newValue);
        }
    }
}
