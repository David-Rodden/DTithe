package nodes;

import org.powbot.api.Condition;
import org.powbot.api.rt4.Chat;
import org.powbot.api.rt4.ChatOption;
import org.powbot.api.rt4.Objects;
import structure.TreeTask;

/**
 * Enter the farm when player has seeds to farm
 */
public class EnterFarm extends TreeTask {
    public EnterFarm() {
        super(true);
    }

    @Override
    public int execute() {
        if (!Chat.chatting()) Objects.stream().within(15).name("Farm door").action("Open").findAny().ifPresent(d -> {
            if(!d.interact("Open")) return;
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
