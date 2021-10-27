package nodes;

import org.powbot.api.rt4.Inventory;
import structure.PositionRecord;
import structure.TreeTask;

import java.util.regex.Pattern;

public class ShouldDeposit extends TreeTask {
    private final PositionRecord record;
    private final Pattern fruit;

    public ShouldDeposit(final PositionRecord record) {
        super(false);
        this.record = record;
        fruit = PlantInfo.FRUIT.getPattern();
    }

    @Override
    public boolean validate() {
        return record.size() == 0 && Inventory.stream().name(fruit).isNotEmpty();
    }
}
