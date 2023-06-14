package top.offsetmonkey538.offsetconfig538.exception;

/**
 * Exception thrown when something goes wrong while trying
 * to parse or generate offset config.
 */
public class OffsetConfigException extends Exception {

    /**
     * Constructs a new OffsetConfigException.
     *
     * @param message The message.
     * @param args The arguments.
     */
    public OffsetConfigException(String message, Object... args) {
        super(String.format(message, args));
    }
}
