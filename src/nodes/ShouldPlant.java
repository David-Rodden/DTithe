package nodes;

import org.powbot.api.rt4.Inventory;
import structure.PositionRecord;
import structure.TreeTask;

import java.util.regex.Pattern;

public class ShouldPlant extends TreeTask {
    private final PositionRecord record;
    private final Pattern seedling;

    public ShouldPlant(final PositionRecord record) {
        super(false);
        this.record = record;
        seedling = PlantInfo.SEEDLING.getPattern();
    }

    @Override
    public boolean validate() {
        return record.size() < 16 && Inventory.stream().name(seedling).isNotEmpty();
    }
}
