package nodes;

import org.powbot.api.rt4.Objects;
import structure.TreeTask;

public class IsInFarm extends TreeTask {
    public IsInFarm() {
        super(false);
    }

    @Override
    public boolean validate() {
        return Objects.stream().within(20).name("Sack").action("Deposit").reachable().isNotEmpty();
    }
}
