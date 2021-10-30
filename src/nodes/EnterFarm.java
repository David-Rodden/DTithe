package nodes;

import org.powbot.api.Condition;
import org.powbot.api.Tile;
import org.powbot.api.rt4.Chat;
import org.powbot.api.rt4.ChatOption;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Objects;
import structure.TreeTask;

/**
 * Enter the farm when player has seeds to farm
 */
public class EnterFarm extends TreeTask {
    private final Tile farmEntrance;

    public EnterFarm() {
        super(true);
        farmEntrance = new Tile(1804, 3501);
    }

    @Override
    public int execute() {
        if (farmEntrance.distance() > 3) {
            Movement.walkTo(farmEntrance);
            Condition.wait(() -> farmEntrance.distance() <= 3, 1500, 3);
            return super.execute();
        }
        if (!Chat.chatting()) Objects.stream().within(15).name("Farm door").action("Open").findAny().ifPresent(d -> {
            if (!d.interact("Open")) return;
            Condition.wait(() -> Objects.stream().within(10).name("Sack").action("Deposit").reachable().isNotEmpty() || Chat.chatting(), 2000, 4);
        });
        if (Chat.canContinue()) {
            Chat.clickContinue();
            return super.execute();
        }
        Chat.stream().textContains("I'm an expert").findAny().ifPresent(ChatOption::select);
        return super.execute();
    }

    @Override
    public String toString() {
        return "Entering farm";
    }
}
