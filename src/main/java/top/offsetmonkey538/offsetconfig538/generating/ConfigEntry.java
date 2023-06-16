package top.offsetmonkey538.offsetconfig538.generating;

/**
 * Represents an entry in OffsetConfig.
 *
 * @param comment The comment for the entry.
 * @param value The value for the entry.
 */
public record ConfigEntry(String comment, Object value) {

    /**
     * Constructs a new ConfigEntry with an empty comment.
     *
     * @param value The value for the entry
     */
    public ConfigEntry(Object value) {
        this("", value);
    }
}
