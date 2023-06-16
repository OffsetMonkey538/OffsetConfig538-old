package top.offsetmonkey538.offsetconfig538.generating;

public record ConfigEntry(String comment, Object value) {

    public ConfigEntry(Object value) {
        this("", value);
    }
}
