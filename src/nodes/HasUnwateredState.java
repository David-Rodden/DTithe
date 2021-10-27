package nodes;

import org.powbot.api.rt4.Objects;
import structure.PositionRecord;
import structure.TreeTask;

import java.util.regex.Pattern;

public class HasUnwateredState extends TreeTask {
    private final PositionRecord record;
    private final Pattern plantPattern;

    public HasUnwateredState(final PositionRecord record) {
        super(false);
        this.record = record;
        plantPattern = PlantInfo.STATES.getPattern();
    }

    @Override
    public boolean validate() {
        final boolean toWater = Objects.stream().within(20).name(plantPattern).action("Water").filter(p -> record.getNotedPosition(p.tile())).isNotEmpty();
        System.out.println("Needs to water: " + toWater);
        return toWater;
    }
}
