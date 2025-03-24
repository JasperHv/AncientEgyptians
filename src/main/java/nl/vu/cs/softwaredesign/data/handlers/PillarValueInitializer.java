package nl.vu.cs.softwaredesign.data.handlers;

import com.almasb.fxgl.dsl.FXGL;
import nl.vu.cs.softwaredesign.data.model.Monarch;
import nl.vu.cs.softwaredesign.data.model.Pillar;

import java.util.EnumMap;
import java.util.Map;

public class PillarValueInitializer {
    private PillarValueInitializer() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Map<Pillar, Integer> initializePillarValues(
            Monarch monarch,
            Map<String, Map<String, Integer>> monarchInitialValues
    ) {
        Map<Pillar, Integer> pillarValues = new EnumMap<>(Pillar.class);
        String monarchName = monarch.getName();
        Map<String, Integer> initialValues = monarchInitialValues.get(monarchName);

        for (Pillar pillar : Pillar.values()) {
            int value = (initialValues != null)
                    ? initialValues.getOrDefault(pillar.getName().toLowerCase(), 0)
                    : 0;
            pillarValues.put(pillar, value);
            FXGL.set(pillar.name(), value);
        }

        return pillarValues;
    }
}