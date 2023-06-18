package top.offsetmonkey538.offsetconfig538.parsing;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import top.offsetmonkey538.offsetconfig538.ConfigEntryWithComment;
import top.offsetmonkey538.offsetconfig538.OffsetConfig538;
import top.offsetmonkey538.offsetconfig538.exception.OffsetConfigException;
import top.offsetmonkey538.offsetconfig538.serialization.OffsetConfigSerializer;

/**
 * Used to parse OffsetConfig content.
 */
public class Parser {
    private int currentLineNumber;
    private String[] lines;

    private final OffsetConfig538 offsetConfig538;

    /**
     * Constructs a new Parser.
     *
     * @param offsetConfig538 the {@link OffsetConfig538} for this parser. Used to get serializers.
     */
    public Parser(OffsetConfig538 offsetConfig538) {
        this.offsetConfig538 = offsetConfig538;
    }

    /**
     * Parses the provided config content into a map of String key to Object value.
     *
     * @param content The config content. Should contain line endings.
     * @return the provided content as a map of String key to Object value.
     * @throws OffsetConfigException when something goes wrong when parsing the content.
     * @see #parse(String)
     */
    public Map<String, Object> parseWithoutComments(String content) throws OffsetConfigException {
        final Map<String, ConfigEntryWithComment> entries = parse(content);
        final Map<String, Object> entriesWithoutComments = new LinkedHashMap<>(entries.size());

        for (Map.Entry<String, ConfigEntryWithComment> entry : entries.entrySet()) {
            entriesWithoutComments.put(entry.getKey(), entry.getValue().value());
        }

        return entriesWithoutComments;
    }

    /**
     * Parses the provided config content into a map of String key to {@link ConfigEntryWithComment} value.
     *
     * @param content The config content. Should contain line endings.
     * @return the provided content as a map of String key to {@link ConfigEntryWithComment} value.
     * @throws OffsetConfigException when something goes wrong when parsing the content.
     * @see #parseWithoutComments(String)
     */
    public Map<String, ConfigEntryWithComment> parse(String content) throws OffsetConfigException {
        final Map<String, ConfigEntryWithComment> entries = new LinkedHashMap<>();

        // Split content at CR/LF (Windows line-ending) or LF (Unix line-ending).
        this.lines = content.split("\\r?\\n");

        // Store the current parent in a stack.
        final Stack<String> parentStack = new Stack<>();

        // Store the comment
        String comment = "";

        for (currentLineNumber = 0; currentLineNumber < lines.length; currentLineNumber++) {
            String line = lines[currentLineNumber];
            String trimmedLine = line.trim();

            // Skip empty lines.
            if (trimmedLine.isEmpty()) {
                continue;
            }

            // Store comment for later.
            if (trimmedLine.startsWith(OffsetConfig538.COMMENT_PREFIX)) {
                comment = trimmedLine.substring(1).trim();
                continue;
            }

            // Get our indentation level.
            final int indentLevel = getIndentation(line);

            // If we are on a lower level of indentation from our parent,
            // then it isn't our parent.
            while (!parentStack.isEmpty() && indentLevel < parentStack.size()) {
                parentStack.pop();
            }

            // Get our key
            String key = getKey(line);
            if (!parentStack.isEmpty()) {
                key = parentStack.peek() + OffsetConfig538.KEY_SEPARATOR + key;
            }

            // Check if we are a parent and add ourselves to the parent stack.
            if (trimmedLine.endsWith(OffsetConfig538.BLOCK_START_INDICATOR)) {
                parentStack.push(key);
                continue;
            }

            // Parse the value and add it to the entries map
            Object value = parseValue(line);
            entries.put(key, new ConfigEntryWithComment(comment, value));
        }

        return entries;
    }

    /**
     * Parses the provided line into an Object.
     *
     * @param line the line of config content. Should be a key-value pair
     * @return the provided line as an Object.
     * @throws OffsetConfigException when something goes wrong when parsing the content.
     */
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

    /**
     * Parses an array in the config into an Object array.
     * Continues reading the config until the end of the array is reached.
     *
     * @param type the type of the array content.
     * @return an Object array from the config.
     * @throws OffsetConfigException when something goes wrong when parsing the content.
     */
    private Object[] parseArray(String type) throws OffsetConfigException {
        List<Object> arrayContent = new ArrayList<>();

        // Increment current line as the current line
        // contains the definition for this array.
        currentLineNumber++;

        for (; currentLineNumber < lines.length; currentLineNumber++) {
            String line = lines[currentLineNumber].trim();

            // Arrays of arrays aren't supported.
            if (line.endsWith(OffsetConfig538.ARRAY_OPEN)) throw new OffsetConfigException("Expected value of type '%s' in array at line '%s', but got another array!", type, currentLineNumber);
            // Nesting isn't supported in arrays.
            if (line.endsWith(OffsetConfig538.BLOCK_START_INDICATOR)) throw new OffsetConfigException("Expected value of type '%s' in array at line '%s', but got a block start!", type, currentLineNumber);
            // Array close is the end of an array.
            if (line.endsWith(OffsetConfig538.ARRAY_CLOSE)) break;

            // Parse the value and put it into the array content.
            arrayContent.add(parseArrayValue(line, type));
        }

        // Return the arrayContent as an array.
        return arrayContent.toArray();
    }

    /**
     * Parses an array value of the provided type.
     *
     * @param value The value to be parsed.
     * @param type The type the value should be.
     * @return the provided value as an Object.
     * @throws OffsetConfigException when something goes wrong when parsing the content.
     */
    private Object parseArrayValue(String value, String type) throws OffsetConfigException {
        // Handle array of objects
        if (value.equals(OffsetConfig538.OBJECT_OPEN)) return parseObject(type);

        if (type.equals("int")) return Integer.parseInt(value);
        if (type.equals("float")) return Float.parseFloat(value);
        if (type.equals("boolean")) return Boolean.parseBoolean(value);
        if (type.equals("string")) {
            if (value.startsWith("\"") && value.endsWith("\"")) return value.substring(1, value.length() - 1);
            throw new OffsetConfigException("Expected double quotes (\") around string array value '%s' at line '%s'!", value, currentLineNumber);
        }

        throw new OffsetConfigException("Invalid value '%s' in array at line '%s'!", value, currentLineNumber);
    }

    /**
     * Parses an object in the config into an Object.
     * Continues reading the config until the end of the object is reached.
     *
     * @param type the type of the object.
     * @return an Object from the config.
     * @throws OffsetConfigException when something goes wrong when parsing the content.
     */
    private Object parseObject(String type) throws OffsetConfigException {
        Map<String, Object> objectContent = new LinkedHashMap<>();

        int numObjectOpen = 1;
        int numObjectClose = 0;

        // Increment current line as we don't want to
        // continuously parse this object and get a stack overflow.
        currentLineNumber++;

        for (; currentLineNumber < lines.length; currentLineNumber++) {
            String line = lines[currentLineNumber].trim();

            // Increment object open and close counters.
            if (line.endsWith(OffsetConfig538.OBJECT_OPEN)) numObjectOpen++;
            if (line.endsWith(OffsetConfig538.OBJECT_CLOSE)) numObjectClose++;

            // The object is finished when we encounter
            // an equal number of open and close characters.
            if (numObjectOpen == numObjectClose) break;

            // Nesting isn't supported in objects.
            if (line.endsWith(OffsetConfig538.BLOCK_START_INDICATOR)) throw new OffsetConfigException("Expected value of type '%s' in object at line '%s', but got a block start!", type, currentLineNumber);

            // Skip array and object endings as those aren't values.
            if (line.endsWith(OffsetConfig538.OBJECT_CLOSE) || line.endsWith(OffsetConfig538.ARRAY_CLOSE)) continue;

            // Parse the value.
            String key = getKey(line);
            objectContent.put(key, parseValue(line));
        }

        // Get the serializer for the type.
        OffsetConfigSerializer<?> serializer = offsetConfig538.getSerializer(type);

        // Check if the serializer wasn't found and throw an error.
        if (serializer == null) throw new OffsetConfigException("No deserializer found for type '%s'!", type);

        // Use the serializer to turn the object content into the actual object.
        return serializer.deserialize(objectContent);
    }

    /**
     * Gets the indentation of the provided line.
     *
     * @param line The line to get the indentation of.
     * @return The indentation level of the provided line.
     */
    private int getIndentation(String line) {
        int amountOfSpaces = line.length() - line.stripLeading().length();
        return amountOfSpaces / OffsetConfig538.INDENTATION_SIZE;
    }

    /**
     * Gets the key of the provided line.
     *
     * @param line The line to get the key of.
     * @return The key of the line.
     * @throws OffsetConfigException when something goes wrong when parsing the content.
     */
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
