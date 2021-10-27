package nodes;

import org.powbot.api.rt4.Objects;
import structure.PositionRecord;
import structure.TreeTask;

import java.util.regex.Pattern;

public class ShouldTend extends TreeTask {
    private final PositionRecord record;
    private final Pattern plantPattern;

    public ShouldTend(final PositionRecord record) {
        super(false);
        this.record = record;
        plantPattern = PlantInfo.STATES.getPattern();
    }

    @Override
    public boolean validate() {
        return Objects.stream().within(20).filter(p -> record.getNotedPosition(p.tile())).name(plantPattern).action("Water", "Harvest").isNotEmpty();
    }
}
