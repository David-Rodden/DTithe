import com.google.common.eventbus.Subscribe;
import nodes.*;
import org.powbot.api.Tile;
import org.powbot.api.event.RenderEvent;
import org.powbot.api.rt4.Objects;
import org.powbot.api.rt4.Players;
import org.powbot.api.rt4.walking.model.Skill;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.mobile.drawing.Graphics;
import org.powbot.mobile.service.ScriptUploader;
import structure.TreeScript;
import structure.TreeTask;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@ScriptManifest(name = "DTithe", description = "Simple test", version = "0.0.1")
public class Tithe extends TreeScript {
    private ScheduledExecutorService cleanupExecutor;
    private ScheduledFuture<?> cleanupSchedule;

    public static void main(String[] args) {
        new ScriptUploader().uploadAndStart("DTithe", "Account", "127.0.0.1:5555", true, true);
    }

    @Override
    public void onStart() {
        // Build our decision tree with its TreeTasks
        final TreeTask head = new ShouldDeposit(this);
        TreeTask second = head.setLeft(new NeedSeeds(this));
        TreeTask third = second.setLeft(new HasEnoughWater());
        third.setLeft(new FillWater());
        TreeTask fourth = third.setRight(new ShouldTend(this));
        TreeTask fifth = fourth.setLeft(new ShouldPlant(this));
        fifth.setLeft(new Idle());
        TreeTask sixth = fifth.setRight(new IsInFarm());
        sixth.setLeft(new EnterFarm());
        sixth.setRight(new PlantSeed(this));
        fourth.setRight(new TendPlant(this));
        second.setRight(new FetchSeeds());
        head.setRight(new DepositFruit());
        // Create a separate threaded task that will clean up tiles with plants no longer occupying them
        // Clean up at one-second intervals
        cleanupExecutor = Executors.newSingleThreadScheduledExecutor();
        cleanupSchedule = cleanupExecutor.scheduleAtFixedRate(() -> {
            if (Players.local().animation() != -1) return;
            Objects.stream().filter(p -> {
                final Tile focusedTile = p.tile();
                return getNotedPosition(focusedTile) && focusedTile.distance() >= 2;
            }).name("Tithe patch").forEach(p -> removeNotedPosition(p.tile()));
        }, 0, 1, TimeUnit.SECONDS);
        // Set our decision tree head for execution
        setHead(head);
        // Track our farming experience and level as well as plants within our paint
        addPaint(PaintBuilder.newBuilder().trackSkill(Skill.Farming).addString("Task: ", this::getTaskDescription).addString(() -> "Planted: " + size()).build());
    }

    @Override
    public void onStop() {
        // Stop our scheduled cleanup process when script halts
        cleanupSchedule.cancel(true);
        cleanupExecutor.shutdown();
    }

    @Override
    public void poll() {
        if (!hasHeadBeenSet()) return;
        traverseTree();
        super.poll();
    }

    @Subscribe
    public void onRender(RenderEvent r) {
        final Graphics g = r.getGraphics();
        // Draw recorded plant tiles with elapsed time
        drawTiles(g);
    }
}
