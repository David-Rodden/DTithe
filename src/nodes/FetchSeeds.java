package nodes;

import org.powbot.api.Area;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;

import java.util.regex.Pattern;

/**
 * Leave farm to fetch seeds from lobby table
 * If outside of farm zone, attempt to webwalk to lobby
 */
public class FetchSeeds extends WalkableTask {
    private final Area lobbyArea;
    private final Pattern seedPattern;

    public FetchSeeds() {
        super();
        seedPattern = PlantInfo.SEEDLING.getPattern();
        lobbyArea = new Area(new Tile(1796, 3505),
                new Tile(1804, 3505),
                new Tile(1804, 3502),
                new Tile(1805, 3502),
                new Tile(1805, 3501),
                new Tile(1804, 3501),
                new Tile(1804, 3498),
                new Tile(1796, 3498));
    }

    @Override
    public int execute() {
        if (Components.stream().textContains("What kind of crop will you grow?").isNotEmpty()) {
            // Decide which seeds to grab based on our farming level
            final int farmingLevel = Skills.realLevel(Constants.SKILLS_FARMING);
            Chat.stream().filter(o -> {
                final String choice = o.text();
                if (farmingLevel >= 74) return choice.contains("Logavano");
                if (farmingLevel >= 54) return choice.contains("Bologano");
                return choice.contains("Golovanova");
            }).findAny().ifPresent(c -> {
                c.select();
                Condition.wait(() -> Inventory.stream().name(seedPattern).isNotEmpty(), 1500, 3);
            });
        } else if (lobbyArea.contains(Players.local())) {
            // Interact with seed table until options dialogue appears
            Objects.stream().within(10).name("Seed table").action("Search").findAny().ifPresent(s -> {
                if(!s.interact("Search"))   return;
                Condition.wait(() -> Chat.stream().textContains("What kind of crop will you grow?").isNotEmpty(), Random.nextInt(800, 1200), 3);
            });
            return super.execute();
        }
        // Tithe is instance area, so if we cannot find a depositable sack for our fruit
        // Assume we are not in tithe farm - execute webwalk to lobby area
        if (Objects.stream().within(20).name("Sack").action("Deposit").reachable().isEmpty()) {
            Movement.walkTo(lobbyArea.getRandomTile());
            return super.execute();
        }
        // Interact with farm door to leave tithe farm to grab appropriate seeds
        Objects.stream().within(15).name("Farm door").action("Open").findAny().ifPresent(d -> {
            if (walkToOrTurn(d)) return;
            if(!d.interact("Open")) return;
            Condition.wait(() -> lobbyArea.contains(Players.local()), 1500, 3);
        });
        return super.execute();
    }

    @Override
    public String toString() {
        return "Fetching appropriate seeds";
    }
}
