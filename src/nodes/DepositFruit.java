package nodes;

import org.powbot.api.Condition;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Objects;

import java.util.regex.Pattern;


/**
 * Deposits fruit within the sack nearby on-start or when out of seeds
 */
public class DepositFruit extends WalkableTask {
    private final Pattern fruitPattern;

    public DepositFruit() {
        fruitPattern = PlantInfo.FRUIT.getPattern();
    }

    @Override
    public int execute() {
        Objects.stream().name("Sack").action("Deposit").findAny().ifPresent(s -> {
            if (walkToOrTurn(s)) return;
            s.interact("Deposit");
            Condition.wait(() -> Inventory.stream().name(fruitPattern).isEmpty(), 1000, 5);
        });
        return super.execute();
    }

    @Override
    public String toString() {
        return "Depositing fruit";
    }
}
