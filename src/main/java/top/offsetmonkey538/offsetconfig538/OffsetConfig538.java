package top.offsetmonkey538.offsetconfig538;

import java.util.HashMap;
import java.util.Map;
import top.offsetmonkey538.offsetconfig538.generating.Generator;
import top.offsetmonkey538.offsetconfig538.parsing.Parser;
import top.offsetmonkey538.offsetconfig538.serialization.OffsetConfigSerializer;

/**
 * Stores constants for the config format and is used to register serializers.
 * Has get methods for a {@link Parser}.
 */
public class OffsetConfig538 {
    /**
     * The indentation size.
     */
    public static final int INDENTATION_SIZE = 4;
    /**
     * The character that opens an array.
     */
    public static final String ARRAY_OPEN = "[";
    /**
     * The character that closes an array.
     */
    public static final String ARRAY_CLOSE = "]";
    /**
     * The character that opens an object.
     */
    public static final String OBJECT_OPEN = "{";
    /**
     * The character that closes an object.
     */
    public static final String OBJECT_CLOSE = "}";
    /**
     * The character that separates a key and a value.
     */
    public static final String KEY_VALUE_DELIMITER = "=";
    /**
     * The character that indicates the start of a new block.
     */
    public static final String BLOCK_START_INDICATOR = ":";
    /**
     * The character that prefixes the type of an array or object.
     */
    public static final String TYPE_PREFIX = "T";
    /**
     * The character that prefixes a comment.
     */
    public static final String COMMENT_PREFIX = "#";
    /**
     * The character that separates keys in nested structures.
     */
    public static final String KEY_SEPARATOR = ".";


    private final Map<String, OffsetConfigSerializer<?>> serializersByType = new HashMap<>();
    private final Map<Class<?>, OffsetConfigSerializer<?>> serializersByTypeClass = new HashMap<>();

    /**
     * Adds the provided serializers to both of the serializer maps.
     *
     * @param serializers The serializers to add.
     * @return this.
     */
    public OffsetConfig538 addSerializer(OffsetConfigSerializer<?>... serializers) {
        for (OffsetConfigSerializer<?> serializer : serializers) {
            this.serializersByType.put(serializer.getType(), serializer);
            this.serializersByTypeClass.put(serializer.getTypeClass(), serializer);
        }
        return this;
    }

    /**
     * Used to get the serializer for the provided type.
     *
     * @param type the type the serializer should be for.
     * @return the serializer for the provided type.
     * @see #getSerializerByTypeClass(Class)
     */
    public OffsetConfigSerializer<?> getSerializerByType(String type) {
        return this.serializersByType.get(type);
    }

    /**
     * Used to get the serializer for the provided type class.
     *
     * @param typeClass the type class the serializer should be for.
     * @return the serializer for the provided type class.
     * @see #getSerializerByType(String)
     */
    public OffsetConfigSerializer<?> getSerializerByTypeClass(Class<?> typeClass) {
        return this.serializersByTypeClass.get(typeClass);
    }

    /**
     * Creates a new {@link Parser}.
     *
     * @return a new {@link Parser}.
     */
    public Parser getParser() {
        return new Parser(this);
    }

    /**
     * Creates a new {@link Generator}.
     *
     * @return a new {@link Generator}.
     */
    public Generator getGenerator() {
        return new Generator(this);
    }
}
