package nodes;

import org.powbot.api.rt4.Inventory;
import structure.PositionRecord;
import structure.TreeTask;

import java.util.regex.Pattern;

public class NeedSeeds extends TreeTask {
    private final Pattern seedling;
    private final PositionRecord record;

    public NeedSeeds(final PositionRecord record) {
        super(false);
        this.record = record;
        seedling = PlantInfo.SEEDLING.getPattern();
    }

    @Override
    public boolean validate() {
        return record.size() == 0 && Inventory.stream().name(seedling).isEmpty();
    }
}
