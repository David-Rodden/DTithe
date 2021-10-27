package nodes;

import org.powbot.api.rt4.Inventory;
import structure.TreeTask;

import java.util.regex.Pattern;

public class HasEnoughWater extends TreeTask {
    private final Pattern wateringCan;

    public HasEnoughWater() {
        super(false);
        wateringCan = PlantInfo.WATERING.getPattern();
    }

    @Override
    public boolean validate() {
        return Inventory.stream().name(wateringCan).isNotEmpty();
    }
}
