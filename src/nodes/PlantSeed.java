package nodes;

import org.powbot.api.Condition;
import org.powbot.api.Locatable;
import org.powbot.api.Random;
import org.powbot.api.Tile;
import org.powbot.api.rt4.Camera;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Objects;
import org.powbot.api.rt4.Players;
import structure.PositionRecord;
import structure.TreeTask;

import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * Plant new seed from our inventory when appropriate, with optimal positioning for reachability and efficiency
 */
public class PlantSeed extends TreeTask {
    private final Pattern seedPattern;
    private final PositionRecord record;

    public PlantSeed(final PositionRecord record) {
        super(true);
        this.record = record;
        seedPattern = PlantInfo.SEEDLING.getPattern();
    }

    @Override
    public int execute() {
        Inventory.stream().name(seedPattern).findAny().ifPresent(s -> {
            // Calculate average positioning of all tiles
            final Locatable clusterAverage = record.getClusterAverage();
            // Find any patch within our vicinity that is closest to our cluster to place our next seed
            Objects.stream().within(15).name("Tithe patch").min(Comparator.comparing(o -> clusterAverage.tile().distanceTo(o.tile()))).ifPresent(p -> {
                if (!p.inViewport()) Camera.turnTo(p);
                if (!s.interact("Use")) return;
                Condition.wait(() -> Inventory.selectedItemIndex() == s.inventoryIndex(), 500, 3);
                if (Inventory.selectedItemIndex() != s.inventoryIndex()) return;
                if (!p.interact("Use")) return;
                final Tile seedlingTile = p.tile();
                Condition.wait(() -> Players.local().animation() != -1, 400, 10);
                // Add our position and initialize its timer to track progress
                if (Players.local().animation() != -1) record.addNotedPosition(seedlingTile);
                Condition.wait(() -> Objects.stream().within(20).name("Tithe patch").at(seedlingTile).isEmpty(), Random.nextInt(500, 800), 3);
            });
        });
        return super.execute();
    }

    @Override
    public String toString() {
        return "Planting seed";
    }
}
