package top.offsetmonkey538.offsetconfig538.generating;

import java.util.LinkedHashMap;
import java.util.Map;
import top.offsetmonkey538.offsetconfig538.OffsetConfig538;
import top.offsetmonkey538.offsetconfig538.exception.OffsetConfigException;

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
}
