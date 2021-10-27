package structure;


public abstract class TreeTask implements SleepSequence {
    private final boolean isLeaf;
    private TreeTask left, right;

    public TreeTask(final boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    boolean isLeaf() {
        return isLeaf;
    }

    TreeTask getLeft() {
        return left;
    }

    TreeTask getRight() {
        return right;
    }

    public TreeTask setLeft(final TreeTask left) {
        return this.left = left;
    }

    public TreeTask setRight(final TreeTask right) {
        return this.right = right;
    }

    public boolean validate() {
        return true;
    }

    public int execute() {
        return getVeryShortSleepTime();
    }

    public void deconstruct() {
    }
}
