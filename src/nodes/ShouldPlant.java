package nodes;

import structure.PositionRecord;
import structure.TreeTask;

public class ShouldPlant extends TreeTask {
    private final PositionRecord record;

    public ShouldPlant(final PositionRecord record) {
        super(false);
        this.record = record;
    }

    @Override
    public boolean validate() {
        return record.size() < 16;
    }
}
