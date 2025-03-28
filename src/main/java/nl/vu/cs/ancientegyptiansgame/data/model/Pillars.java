package nl.vu.cs.ancientegyptiansgame.data.model;

public enum Pillars {
    PRIESTS("priests", "priests.png", "priests-card.png", new Ending("The gods abandon us, Pharaoh! Your neglect of religious rites and offerings angers the temple priests. They declare you a false Pharaoh, and civil unrest splits Egypt into rival factions.", "endings/temple-priest-ending.png")),
    FARMERS("farmers", "farmers.png", "farmers-card.png", new Ending("The nobles rule in all but name, great Pharaoh. By granting them too much power, you create a system where they control the wealth and land. Eventually, they overthrow your rule, and Egypt fractures.", "endings/farmers-ending.png")),
    NOBLES("nobles", "nobles.png", "nobles-card.png", new Ending("The fields are empty, and the monuments crumble. Overworking the farmers and laborers breaks their spirits, leading to revolt. Egypt enters a dark age of ruin and famine.", "endings/nobles-ending.png")),
    MILITARY("military", "military.png", "military-card.png", new Ending("The generals no longer answer to you. Discontent among the commanders leads to a military coup, and a warlord-king takes your place. Egypt enters an age of martial law and unrest.", "endings/military-ending.png"));

    private final String name;
    private final String image;
    private final String cardImage;
    private final Ending ending;

    Pillars(String name, String image, String cardImage, Ending ending) {
        this.name = name;
        this.image = image;
        this.cardImage = cardImage;
        this.ending = ending;
    }

    public String getName() { return name; }
    public String getImage() { return image; }
    public String getCardImage() { return cardImage; }
    public Ending getEnding() { return ending; }

    public static Pillars fromName(String name) {
        for (Pillars pillars : values()) {
            if (pillars.getName().equalsIgnoreCase(name)) {
                return pillars;
            }
        }
        throw new IllegalArgumentException("Unknown Pillars: " + name);
    }
}
