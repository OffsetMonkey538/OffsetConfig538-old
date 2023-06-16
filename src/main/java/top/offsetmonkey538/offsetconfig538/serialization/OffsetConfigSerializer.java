package top.offsetmonkey538.offsetconfig538.serialization;

import java.lang.reflect.ParameterizedType;
import java.util.Map;
import top.offsetmonkey538.offsetconfig538.exception.OffsetConfigException;

/**
 * A serializer for a specific class.
 * Implement this class to create
 * a serializer for a custom class.
 *
 * @param <T> The class to serialize.
 */
public interface OffsetConfigSerializer<T> {

    /**
     * Creates a new instance of <code>T</code> from the provided entries.
     *
     * @param entries The entries to construct the object from.
     * @return a new object of type T.
     * @throws OffsetConfigException when something goes wrong when deserializing the content.
     */
    T deserialize(Map<String, Object> entries) throws OffsetConfigException;

    /**
     * Serializes the provided Object value into the provided entries Map.
     * <br>
     * Checks if <code>value</code> is of the generic type {@link T} and calls the {@link #serialize(Map, T)} method.
     *
     * @param entries The entries map to put the data into.
     * @param value The value to serialize.
     * @throws OffsetConfigException When <code>value</code> isn't of the generic type {@link T} or when something goes wrong while serializing.
     */
    @SuppressWarnings("unchecked")
    default void serializeFromObject(Map<String, Object> entries, Object value) throws OffsetConfigException {
        if (!value.getClass().isAssignableFrom(getTypeClass())) throw new OffsetConfigException("Value '%s' not instance of '%s'!", value, getTypeClass());
        serialize(entries, (T) value);
    }

    /**
     * Serializes the provided Object value into the provided entries Map.
     * <br>
     * Example implementation:
     * <pre> {@code
     *      public void serialize(Map<String, Object> entries, MyCoolClass value) throws OffsetConfigException {
     *          entries.put("veryCoolValue", value.getVeryCoolValue));
     *          entries.put("anotherCoolValue", value.getAnotherCoolValue());
     *      }
     * }
     * </pre>
     *
     * @param entries The entries map to put the data into.
     * @param value The value to serialize.
     * @throws OffsetConfigException When something goes wrong while serializing.
     */
    void serialize(Map<String, Object> entries, T value) throws OffsetConfigException;

    /**
     * Get the <code>type</code> this serializer serializes.
     *
     * @return the <code>type</code> this serializer serializes.
     */
    default String getType() {
        return getTypeClass().getName();
    }

    /**
     * Get the class of <code>T</code>.
     *
     * @return the class of <code>T</code>.
     */
    default Class<?> getTypeClass() {
        return (Class<?>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
    }
}
