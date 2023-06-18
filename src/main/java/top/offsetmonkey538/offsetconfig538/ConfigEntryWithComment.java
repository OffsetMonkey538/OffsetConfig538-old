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
}
