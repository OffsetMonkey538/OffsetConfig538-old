package top.offsetmonkey538.offsetconfig538;

/**
 * Represents an entry in OffsetConfig with a comment.
 *
 * @param comment The comment for the entry.
 * @param value The value for the entry.
 */
public record ConfigEntryWithComment(String comment, Object value) {

    /**
     * Constructs a new ConfigEntry with an empty comment.
     *
     * @param value The value for the entry
     */
    public ConfigEntryWithComment(Object value) {
        this("", value);
    }

    /**
     * Constructs a new ConfigEntry with an empty value. Useful if you want to add comments to blocks.
     *
     * @param comment The comment for the entry.
     */
    public ConfigEntryWithComment(String comment) {
        this(comment, null);
    }
}
