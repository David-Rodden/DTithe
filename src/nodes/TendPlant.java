package nodes;

import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.Tile;
import org.powbot.api.rt4.Objects;
import org.powbot.api.rt4.Players;
import structure.PositionRecord;

import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * Water or harvest plant that requires attention, prioritizing oldest plants
 */
public class TendPlant extends WalkableTask {
    private final PositionRecord record;
    private final Pattern plantPattern;

    public TendPlant(final PositionRecord record) {
        this.record = record;
        plantPattern = PlantInfo.STATES.getPattern();
    }

    @Override
    public int execute() {
        // Find any plants requiring water or harvesting, and prioritize by time elapsed since tended for
        Objects.stream().filter(p -> record.getNotedPosition(p.tile())).action("Water", "Harvest").max(Comparator.comparing(record::getElapsed)).ifPresent(p -> {
            if (walkToOrTurn(p)) return;
            p.interact(c -> c.getAction().matches("Water|Harvest"), false);
            Condition.wait(() -> Players.local().animation() != -1, Random.nextInt(500, 800), 1);
            final Tile focusedTile = p.tile();
            // Once we tend to our plant, make sure to reset its timer so other plants can be prioritized
            if (Players.local().animation() != -1 && focusedTile.distance() <= 3) {
                record.resetTimer(focusedTile);
                Condition.sleep(Random.nextInt(400, 600));
            }
        });
        return super.execute();
    }

    @Override
    public String toString() {
        return "Tending to plant";
    }
}
