package nl.vu.cs.softwaredesign.data.model;

public enum Pillar {
    PRIESTS("priests", "priests.png", new Ending("The gods abandon us, Pharaoh! Your neglect of religious rites and offerings angers the temple priests. They declare you a false Pharaoh, and civil unrest splits Egypt into rival factions.", "endings/temple-priest-ending.png")),
    FARMERS("farmers", "farmers.png", new Ending("The nobles rule in all but name, great Pharaoh. By granting them too much power, you create a system where they control the wealth and land. Eventually, they overthrow your rule, and Egypt fractures.", "endings/farmers-ending.png")),
    NOBLES("nobles", "nobles.png", new Ending("The fields are empty, and the monuments crumble. Overworking the farmers and laborers breaks their spirits, leading to revolt. Egypt enters a dark age of ruin and famine.", "endings/nobles-ending.png")),
    MILITARY("military", "military.png", new Ending("The generals no longer answer to you. Discontent among the commanders leads to a military coup, and a warlord-king takes your place. Egypt enters an age of martial law and unrest.", "endings/military-ending.png"));

    private final String name;
    private final String image;
    private final Ending ending;

    Pillar(String name, String image, Ending ending) {
        this.name = name;
        this.image = image;
        this.ending = ending;
    }

    public String getName() { return name; }
    public String getImage() { return image; }
    public Ending getEnding() { return ending; }

    public static Pillar fromName(String name) {
        for (Pillar pillar : values()) {
            if (pillar.getName().equalsIgnoreCase(name)) {
                return pillar;
            }
        }
        throw new IllegalArgumentException("Unknown Pillar: " + name);
    }
}
