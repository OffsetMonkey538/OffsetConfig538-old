package top.offsetmonkey538.offsetconfig538.parsing;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import top.offsetmonkey538.offsetconfig538.OffsetConfig538;
import top.offsetmonkey538.offsetconfig538.exception.OffsetConfigException;

public class Parser {
    private int currentLineNumber;
    private String[] lines;

    private final OffsetConfig538 offsetConfig538;

    public Parser(OffsetConfig538 offsetConfig538) {
        this.offsetConfig538 = offsetConfig538;
    }

    public Map<String, Object> parse(String content) throws OffsetConfigException {
        final Map<String, Object> entries = new LinkedHashMap<>();

        // Split content at CR/LF (Windows line-ending) or LF (Unix line-ending).
        this.lines = content.split("\\r?\\n");

        // Store the current parent in a stack.
        final Stack<String> parentStack = new Stack<>();

        for (currentLineNumber = 0; currentLineNumber < lines.length; currentLineNumber++) {
            String line = lines[currentLineNumber];
            String trimmedLine = line.trim();

            // Skip empty lines and comments.
            if (trimmedLine.isEmpty() || trimmedLine.startsWith(OffsetConfig538.COMMENT_PREFIX)) {
                continue;
            }

            // Get our indentation level
            final int indentLevel = getIndentation(line);

            // If we are on a lower level of indentation from our parent,
            // then it isn't our parent.
            while (!parentStack.isEmpty() && indentLevel < parentStack.size()) {
                parentStack.pop();
            }

            // Get our key
            String key = getKey(line);
            if (!parentStack.isEmpty()) {
                key = parentStack.peek() + "." + key;
            }

            // Check if we are a parent and add ourselves to the parent stack.
            if (trimmedLine.endsWith(OffsetConfig538.BLOCK_START_INDICATOR)) {
                parentStack.push(key);
                continue;
            }

            // Parse the value and add it to the entries map
            Object value = parseValue(line);
            entries.put(key, value);
        }

        return entries;
    }

    private Object parseValue(String line) throws OffsetConfigException {
        // The value starts after the equals sign and ends at the end of the line.
        String valueString = line.substring(
                line.indexOf(OffsetConfig538.KEY_VALUE_DELIMITER) + 1
        ).trim();

        // String if value starts and ends with double quotes (").
        if (valueString.startsWith("\"") && valueString.endsWith("\"")) return valueString.substring(1, valueString.length() - 1);
        // Integer if value is a number without a decimal point.
        if (valueString.matches("\\d+")) return Integer.parseInt(valueString);
        // Float if value is a number with a decimal point.
        if (valueString.matches("\\d+\\.\\d+")) return Float.parseFloat(valueString);
        // Boolean true if value is "true".
        if (valueString.equalsIgnoreCase("true")) return true;
        // Boolean false if value is "false".
        if (valueString.equalsIgnoreCase("false")) return false;


        // Objects and arrays need to define their type.
        if (valueString.startsWith(OffsetConfig538.TYPE_PREFIX)) {
            // Type starts after the type prefix.
            int typeStart = valueString.indexOf(OffsetConfig538.TYPE_PREFIX) + 1;
            // and ends before the last character which is either
            // a key-value delimiter or a block start indicator.
            int typeEnd = valueString.length() - 1;

            String type = valueString.substring(typeStart, typeEnd).trim();

            // Array if value ends with array open.
            if (valueString.endsWith(OffsetConfig538.ARRAY_OPEN)) return parseArray(type);

            // Object if value ends with object open.
            if (valueString.endsWith(OffsetConfig538.OBJECT_OPEN)) return parseObject(type);

            throw new OffsetConfigException("Expected '%s' or '%s' at the end of '%s' at line '%s'!",
                    OffsetConfig538.KEY_VALUE_DELIMITER,
                    OffsetConfig538.BLOCK_START_INDICATOR,
                    valueString,
                    currentLineNumber
            );
        }

        throw new OffsetConfigException("Invalid value '%s' at line '%s'!", valueString, currentLineNumber);
    }

    private int getIndentation(String line) {
        int amountOfSpaces = line.length() - line.stripLeading().length();
        return amountOfSpaces / OffsetConfig538.INDENTATION_SIZE;
    }

    private String getKey(String line) throws OffsetConfigException {
        // The end of the key is either a key-value
        // delimiter or a block start indicator.
        int keyEnd = line.indexOf(OffsetConfig538.KEY_VALUE_DELIMITER);
        if (keyEnd == -1) keyEnd = line.indexOf(OffsetConfig538.BLOCK_START_INDICATOR);

        if (keyEnd == -1)
            throw new OffsetConfigException("Expected '%s' or '%s' in '%s' at line '%s'!", OffsetConfig538.KEY_VALUE_DELIMITER, OffsetConfig538.BLOCK_START_INDICATOR, line, currentLineNumber);

        String key = line.substring(0, keyEnd).trim();
        if (key.equals(""))
            throw new OffsetConfigException("Expected key in '%s' at line '%s'!", line, currentLineNumber);

        return key;
    }
}
