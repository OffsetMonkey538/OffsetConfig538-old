package top.offsetmonkey538.offsetconfig538;

import java.util.HashMap;
import java.util.Map;
import top.offsetmonkey538.offsetconfig538.serialization.OffsetConfigSerializer;

public class OffsetConfig538 {
    public static final int INDENTATION_SIZE = 4;
    public static final String ARRAY_OPEN = "[";
    public static final String ARRAY_CLOSE = "]";
    public static final String OBJECT_OPEN = "{";
    public static final String OBJECT_CLOSE = "}";
    public static final String KEY_VALUE_DELIMITER = "=";
    public static final String BLOCK_START_INDICATOR = ":";
    public static final String TYPE_PREFIX = "T";
    public static final String COMMENT_PREFIX = "#";


    private final Map<String, OffsetConfigSerializer<?>> serializers = new HashMap<>();

    public OffsetConfig538 addSerializer(OffsetConfigSerializer<?>... serializers) {
        for (OffsetConfigSerializer<?> serializer : serializers) {
            this.serializers.put(serializer.getType(), serializer);
        }
        return this;
    }

    public OffsetConfigSerializer<?> getSerializer(String className) {
        return this.serializers.get(className);
    }
}
