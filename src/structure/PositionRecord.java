package structure;


import org.powbot.api.Locatable;
import org.powbot.mobile.drawing.Graphics;

public interface PositionRecord {
    void addNotedPosition(final Locatable tile);

    void removeNotedPosition(final Locatable tile);

    boolean getNotedPosition(final Locatable tile);

    Locatable getClusterAverage();

    long getElapsed(final Locatable tile);

    void resetTimer(final Locatable tile);

    void drawTiles(final Graphics g);

    int size();

    String allTiles();
}
