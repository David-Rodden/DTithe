package structure;

import com.google.common.base.Stopwatch;
import org.powbot.api.*;
import org.powbot.api.rt4.Players;
import org.powbot.api.script.AbstractScript;
import org.powbot.mobile.drawing.Graphics;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ScriptRecord extends AbstractScript implements FlagRecord, NameRecord, PositionRecord, SettingRecord {
    private final Map<String, Boolean> notedFlags;
    private final Map<String, String> notedNames;
    private final Map<Locatable, Stopwatch> notedPositions;
    private final Collection<String> notedSettings;

    ScriptRecord() {
        notedFlags = new ConcurrentHashMap<>();
        notedNames = new ConcurrentHashMap<>();
        notedPositions = new ConcurrentHashMap<>();
        notedSettings = ConcurrentHashMap.newKeySet();
    }

    /**
     * Loop shouldn't do anything, extend this class through your main class
     *
     * @return
     */
    @Override
    public void poll() {
        Condition.sleep(Random.nextInt(80, 140));
    }

    /**
     * Be careful when calling this - it will purge all noted positions, flags & settings
     */
    public void wipeCachedData() {
        notedPositions.clear();
        notedFlags.clear();
        notedSettings.clear();
    }

    @Override
    public void setNotedFlag(String name, boolean value) {
        notedFlags.put(name, value);
    }

    @Override
    public boolean getNotedFlag(final String name) {
        return notedFlags.getOrDefault(name, false);
    }

    @Override
    public void addNotedPosition(Locatable tile) {
        notedPositions.put(tile, Stopwatch.createStarted());
    }

    @Override
    public void removeNotedPosition(final Locatable tile) {
        notedPositions.remove(tile);
    }

    @Override
    public boolean getNotedPosition(Locatable tile) {
        return notedPositions.containsKey(tile);
    }

    @Override
    public Locatable getClusterAverage() {
        final int size = size();
        if (size == 0) return Players.local();
        Supplier<Stream<Locatable>> focusedPositions = () -> notedPositions.keySet().stream();
        final int x = focusedPositions.get().mapToInt(v -> v.tile().x()).sum() / size;
        final int y = focusedPositions.get().mapToInt(v -> v.tile().y()).sum() / size;
        return new Tile(x, y);
    }

    @Override
    public long getElapsed(final Locatable tile) {
        return notedPositions.getOrDefault(tile, Stopwatch.createUnstarted()).elapsed(TimeUnit.SECONDS);
    }

    @Override
    public void resetTimer(final Locatable tile) {
        notedPositions.put(tile, Stopwatch.createStarted());
    }

    @Override
    public void drawTiles(final Graphics g) {
        notedPositions.forEach((k, v) -> ((Tile) k).drawOnScreen(g, v.elapsed(TimeUnit.SECONDS) + "s", Color.getRED(), Color.getCYAN()));
    }

    @Override
    public int size() {
        return notedPositions.size();
    }

    @Override
    public String allTiles() {
        return notedPositions.toString();
    }

    @Override
    public void addNotedSetting(String name) {
        notedSettings.add(name);
    }

    @Override
    public boolean getNotedSetting(String name) {
        return notedSettings.contains(name);
    }

    @Override
    public void addNotedName(String name, String value) {
        notedNames.put(name, value);
    }

    @Override
    public String getNotedName(String name) {
        return notedNames.get(name);
    }
}
