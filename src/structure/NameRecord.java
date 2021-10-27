package structure;

public interface NameRecord {
    void addNotedName(final String name, final String value);

    String getNotedName(final String name);
}
