package nodes;

import org.powbot.api.Condition;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Objects;

import java.util.regex.Pattern;

/**
 * Filling water using nearby water source when our watering cans are empty
 */
public class FillWater extends WalkableTask {
    private final Pattern wateringCan;

    public FillWater() {
        super();
        wateringCan = PlantInfo.WATERING.getPattern();
    }

    @Override
    public int execute() {
        Inventory.stream().name("Gricoller's can", "Watering can").findAny().ifPresent(c -> {
            Objects.stream().within(20).name("Water Barrel").findAny().ifPresent(b -> {
                if (walkToOrTurn(b)) return;
                if (!c.interact("Use")) return;
                Condition.wait(() -> Inventory.selectedItemIndex() == c.inventoryIndex(), 500, 3);
                if (Inventory.selectedItemIndex() != c.inventoryIndex()) return;
                if(!b.interact("Use"))  return;
                Condition.wait(() -> Inventory.stream().name("Watering can").isEmpty(), 500, 10);
            });
        });
        return super.execute();
    }

    @Override
    public String toString() {
        return "Filling water";
    }
}
