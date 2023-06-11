package top.offsetmonkey538.offsetconfig538.exception;

public class OffsetConfigException extends Exception {

    public OffsetConfigException(String message, Object... args) {
        super(String.format(message, args));
    }
}
