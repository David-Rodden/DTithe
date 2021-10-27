package nodes;

import org.powbot.api.Condition;
import org.powbot.api.Tile;
import org.powbot.api.rt4.Camera;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Movement;
import structure.TreeTask;

import java.util.Comparator;

/**
 * Inheritable task for any walking-related activity
 */
public class WalkableTask extends TreeTask {
    WalkableTask() {
        super(true);
    }

    boolean walkToOrTurn(final GameObject target) {
        final double targetDistance = target.tile().distance();
        if (target.inViewport() && targetDistance <= 7) return false;
        if (targetDistance <= 7) Camera.turnTo(target);
        else walkTo(target);
        return true;
    }

    private void walkTo(final GameObject target) {
        target.getWalkableNeighbours(false).stream().min(Comparator.comparing(Tile::distance)).ifPresent(t -> {
            Movement.walkTo(t);
            Condition.wait(target::inViewport, 500, 6);
        });
    }
}
