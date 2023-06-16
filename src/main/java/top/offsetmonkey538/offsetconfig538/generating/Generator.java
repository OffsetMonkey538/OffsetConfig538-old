package top.offsetmonkey538.offsetconfig538.generating;

import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.Map;
import top.offsetmonkey538.offsetconfig538.OffsetConfig538;
import top.offsetmonkey538.offsetconfig538.exception.OffsetConfigException;
import top.offsetmonkey538.offsetconfig538.serialization.OffsetConfigSerializer;

public class Generator {

    private final OffsetConfig538 offsetConfig538;
    private final String lineSeparator = System.lineSeparator();

    public Generator(OffsetConfig538 offsetConfig538) {
        this.offsetConfig538 = offsetConfig538;
    }

    public String generateFromObjects(Map<String, Object> entries) throws OffsetConfigException {
        Map<String, ConfigEntry> configEntries = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : entries.entrySet()) {
            configEntries.put(entry.getKey(), new ConfigEntry(entry.getValue()));
        }

        return generateFromConfigEntries(configEntries);
    }

    public String generateFromConfigEntries(Map<String, ConfigEntry> entries) throws OffsetConfigException {
        StringBuilder builder = new StringBuilder();

        int lastIndentationLevel = 0;

        // Loop through all entries
        for (Map.Entry<String, ConfigEntry> entry : entries.entrySet()) {
            ConfigEntry configEntry = entry.getValue();
            String comment = configEntry.comment();
            Object value = configEntry.value();

            // Split the key using the KEY_SEPARATOR.
            String[] keys = entry.getKey().split("\\" + OffsetConfig538.KEY_SEPARATOR);
            int indentationLevel = keys.length - 1;

            // Deal with parents
            if (lastIndentationLevel < indentationLevel) {
                for (int i = lastIndentationLevel; i < indentationLevel; i++) {
                    builder.append(getIndentation(i)).append(keys[i]).append(OffsetConfig538.BLOCK_START_INDICATOR).append(lineSeparator);
                }
            }

            // Append comment if it exists
            if (!"".equals(comment)) {
                builder.append(getIndentation(indentationLevel)).append(OffsetConfig538.COMMENT_PREFIX).append(" ").append(comment).append(lineSeparator);
            }
            builder
                    .append(getIndentation(indentationLevel))
                    .append(keys[keys.length - 1])
                    .append(" ")
                    .append(OffsetConfig538.KEY_VALUE_DELIMITER)
                    .append(" ")
                    .append(generateValue(value, indentationLevel))
                    .append(lineSeparator);

            lastIndentationLevel = indentationLevel;
        }

        return builder.toString();
    }

    private String generateValue(Object value, int indentationLevel) throws OffsetConfigException {
        // Strings are surrounded by double quotes (").
        if (value instanceof String) return "\"" + value + "\"";
        if (value instanceof Integer) return value.toString();
        if (value instanceof Float) return value.toString();
        if (value instanceof Boolean) return value.toString();

        // Get the type for the value
        String type = OffsetConfig538.TYPE_PREFIX + getType(value) + " ";

        // Return the type and value.
        if (value.getClass().isArray()) return type + generateArray(value, indentationLevel);
        return type + generateObject(value, indentationLevel);
    }

    private String generateArray(Object value, int indentationLevel) throws OffsetConfigException {
        StringBuilder builder = new StringBuilder();

        // Append array open character.
        builder.append(OffsetConfig538.ARRAY_OPEN).append(lineSeparator);

        for (int i = 0; i < Array.getLength(value); i++) {
            // Append indentation inside of array
            builder.append(getIndentation(indentationLevel + 1));

            // Append value of array
            builder.append(generateArrayValue(Array.get(value, i), indentationLevel + 1)).append("\n");
        }

        // Append indentation and array close character.
        builder.append(getIndentation(indentationLevel)).append(OffsetConfig538.ARRAY_CLOSE);


        return builder.toString();
    }

    private String generateArrayValue(Object value, int indentationLevel) throws OffsetConfigException {
        if (value instanceof String) return "\"" + value + "\"";
        if (value instanceof Integer) return value.toString();
        if (value instanceof Float) return value.toString();
        if (value instanceof Boolean) return value.toString();

        return generateObject(value, indentationLevel);
    }

    private String generateObject(Object value, int indentationLevel) throws OffsetConfigException {
        StringBuilder builder = new StringBuilder();

        // Append object open character.
        builder.append(OffsetConfig538.OBJECT_OPEN).append(lineSeparator);

        // Get the serializer
        String type = getType(value);
        OffsetConfigSerializer<?> serializer = offsetConfig538.getSerializer(type);
        if (serializer == null) throw new OffsetConfigException("No serializer found for type '%s'!", type);

        Map<String, Object> entries = new LinkedHashMap<>();
        serializer.serializeFromObject(entries, value);

        for (Map.Entry<String, Object> entry : entries.entrySet()) {
            // Append indentation inside of object
            builder.append(getIndentation(indentationLevel + 1));


            // Append the key
            builder.append(entry.getKey()).append(" ").append(OffsetConfig538.KEY_VALUE_DELIMITER).append(" ");

            // Append the object
            builder.append(generateValue(entry.getValue(), indentationLevel + 1)).append(lineSeparator);
        }

        // Append indentation and array close character.
        builder.append(getIndentation(indentationLevel)).append(OffsetConfig538.OBJECT_CLOSE);


        return builder.toString();
    }

    private String getIndentation(int indentationLevel) {
        return " ".repeat(indentationLevel * OffsetConfig538.INDENTATION_SIZE);
    }

    private String getType(Object value) {
        Class<?> valueType = value.getClass();
        if (valueType.isArray()) valueType = valueType.getComponentType();

        if (valueType == String.class) return "string";
        if (valueType == Integer.class) return "int";
        if (valueType == Float.class) return "float";
        if (valueType == Boolean.class) return "boolean";

        return valueType.getName();
    }
}
