package structure;

public interface FlagRecord {
    void setNotedFlag(final String name, final boolean value);

    boolean getNotedFlag(final String name);
}
