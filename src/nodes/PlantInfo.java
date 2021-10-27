package nodes;

import java.util.regex.Pattern;

public enum PlantInfo {
    WATERING("Gricoller's can|Watering can\\([1-8]\\)"), SEEDLING("(Golovanova|Bologano|Logavano) seed"), STATES("(Golovanova|Bologano|Logavano) (seedling|plant)"), FRUIT("(Golovanova|Bologano|Logavano) fruit");
    private final Pattern pattern;

    PlantInfo(final String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public Pattern getPattern() {
        return pattern;
    }
}
