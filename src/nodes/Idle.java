package nodes;

import org.powbot.api.Condition;
import org.powbot.api.Random;
import structure.TreeTask;

/**
 * Filler task to make sure we do not get too ambitious and plant over a set amount
 */
public class Idle extends TreeTask {
    public Idle() {
        super(true);
    }

    @Override
    public int execute() {
        Condition.sleep(Random.nextInt(1000, 2000));
        return super.execute();
    }

    @Override
    public String toString() {
        return "Idling";
    }
}
