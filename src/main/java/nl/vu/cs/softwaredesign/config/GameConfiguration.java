package nl.vu.cs.softwaredesign.config;

import nl.vu.cs.softwaredesign.data.*;
import java.util.List;

public class GameConfiguration {

    private List<Pillar> pillars;
    private List<Mode> modes;

    public List<Mode> getModes() {
        return modes;
    }

    public List<Pillar> getPillars() {
        return pillars;
    }

    private GameConfiguration() {}

    public GameConfiguration(GameConfiguration.Builder builder) {
        this.pillars = List.copyOf(builder.pillars);
        this.modes = List.copyOf(builder.modes);
    }

    public static class Builder {
        private List<Pillar> pillars;
        private List<Mode> modes;

        public Builder withPillars(Pillar... pillars) {
            this.pillars = List.of(pillars);
            return this;
        }

        public Builder withModes(Mode... modes) {
            this.modes = List.of(modes);
            return this;
        }

        public GameConfiguration build() {
            if(pillars == null || pillars.isEmpty()) {
                throw new IllegalStateException("pillars can't be null or empty");
            }
            if(modes == null || modes.isEmpty()) {
                throw new IllegalStateException("modes can't be null or empty");
            }
            return new GameConfiguration(this);
        }

    }

}
