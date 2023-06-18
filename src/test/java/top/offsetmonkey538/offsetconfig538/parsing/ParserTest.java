package top.offsetmonkey538.offsetconfig538.parsing;

import org.junit.jupiter.api.Test;
import top.offsetmonkey538.offsetconfig538.OffsetConfig538;
import top.offsetmonkey538.offsetconfig538.exampleClasses.VeryCoolObject;
import top.offsetmonkey538.offsetconfig538.exampleClasses.VeryCoolObjectWithArray;
import top.offsetmonkey538.offsetconfig538.exampleClasses.VeryCoolObjectWithObject;
import top.offsetmonkey538.offsetconfig538.exception.OffsetConfigException;
import top.offsetmonkey538.offsetconfig538.generating.ConfigEntry;
import top.offsetmonkey538.offsetconfig538.util.ArrayUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("TrailingWhitespacesInTextBlock")
public class ParserTest {
    private static final OffsetConfig538 offsetConfig538 = new OffsetConfig538();

    @Test
    public void parseBasicValues() throws OffsetConfigException {
        String config = """
                anInteger = 1234
                aFloat = 12.34
                aTrueBoolean = true
                aFalseBoolean = false
                aString = "Hello, World!"
                """;
        Map<String, Object> expectedOutput = Map.ofEntries(
                Map.entry("anInteger", 1234),
                Map.entry("aFloat", 12.34f),
                Map.entry("aTrueBoolean", true),
                Map.entry("aFalseBoolean", false),
                Map.entry("aString", "Hello, World!")
        );

        runTest(config, expectedOutput);
    }

    @Test
    public void parseBasicValuesWithComments() throws OffsetConfigException {
        String config = """
                # This is an integer
                anInteger = 1234
                # This is a float
                aFloat = 12.34
                # This is a boolean which is true
                aTrueBoolean = true
                # This is a boolean which is false
                aFalseBoolean = false
                # This is a string
                aString = "Hello, World!"
                """;
        Map<String, ConfigEntry> expectedOutput = Map.ofEntries(
                Map.entry("anInteger", new ConfigEntry("This is an integer", 1234)),
                Map.entry("aFloat", new ConfigEntry("This is a float", 12.34f)),
                Map.entry("aTrueBoolean", new ConfigEntry("This is a boolean which is true", true)),
                Map.entry("aFalseBoolean", new ConfigEntry("This is a boolean which is false", false)),
                Map.entry("aString", new ConfigEntry("This is a string", "Hello, World!"))
        );

        runTestWithComments(config, expectedOutput);
    }

    @Test
    public void parseBasicValuesWithCommentsAndEmptyLinesAndWhitespaces() throws OffsetConfigException {
        String config = """
                
                # I am a comment!  
                  
                anInteger = 1234
                aFloat = 12.34              
                aTrueBoolean = true  
                aFalseBoolean = false
                
                
                
                # This is also a comment!
                
                
                
                
                aString = "Hello, World!"  
                """;
        Map<String, Object> expectedOutput = Map.ofEntries(
                Map.entry("anInteger", 1234),
                Map.entry("aFloat", 12.34f),
                Map.entry("aTrueBoolean", true),
                Map.entry("aFalseBoolean", false),
                Map.entry("aString", "Hello, World!")
        );

        runTest(config, expectedOutput);
    }

    @Test
    public void parseNestedBasicValues() throws OffsetConfigException {
        String config = """
                iHaveAnIntegerAndAFloat:  
                    anInteger = 1234
                    aFloat = 12.34
                aTrueBoolean = true
                iHaveABooleanAndAString:
                    aFalseBoolean = false
                    aString = "Hello, World!"
                """;
        Map<String, Object> expectedOutput = Map.ofEntries(
                Map.entry("iHaveAnIntegerAndAFloat.anInteger", 1234),
                Map.entry("iHaveAnIntegerAndAFloat.aFloat", 12.34f),
                Map.entry("aTrueBoolean", true),
                Map.entry("iHaveABooleanAndAString.aFalseBoolean", false),
                Map.entry("iHaveABooleanAndAString.aString", "Hello, World!")
        );

        runTest(config, expectedOutput);
    }

    @Test
    public void parseMultiNestedBasicValues() throws OffsetConfigException {
        String config = """
                iHaveAnIntegerAndANestedFloat:  
                    anInteger = 1234
                    iHaveAFloat:
                        aFloat = 12.34
                aTrueBoolean = true
                iHaveABooleanAndAString:
                    aFalseBoolean = false
                    aString = "Hello, World!"
                """;
        Map<String, Object> expectedOutput = Map.ofEntries(
                Map.entry("iHaveAnIntegerAndANestedFloat.anInteger", 1234),
                Map.entry("iHaveAnIntegerAndANestedFloat.iHaveAFloat.aFloat", 12.34f),
                Map.entry("aTrueBoolean", true),
                Map.entry("iHaveABooleanAndAString.aFalseBoolean", false),
                Map.entry("iHaveABooleanAndAString.aString", "Hello, World!")
        );

        runTest(config, expectedOutput);
    }

    @Test
    public void parseArraysWithBasicValues() throws OffsetConfigException {
        String config = """
                integerArray = Tint [
                    1234
                    4321
                    6789
                    9876
                ]
                floatArray = Tfloat [
                    12.34
                    43.21
                    67.89
                    98.76
                ]
                booleanArray = Tboolean [
                    false
                    true
                    false
                    false
                    true
                    false
                    false
                    false
                    false
                    true
                    true
                    false
                    true
                    false
                    false
                    true
                ]
                stringArray = Tstring [
                    "The last"
                    "array"
                    "totally"
                    "doesn't"
                    "mean"
                    "Hi"
                    "in"
                    "binary!"
                ]
                """;
        Map<String, Object> expectedOutput = Map.ofEntries(
                Map.entry("integerArray", new int[] {
                        1234,
                        4321,
                        6789,
                        9876
                }),
                Map.entry("floatArray", new float[] {
                        12.34f,
                        43.21f,
                        67.89f,
                        98.76f
                }),
                Map.entry("booleanArray", new boolean[] {
                        false,
                        true,
                        false,
                        false,
                        true,
                        false,
                        false,
                        false,
                        false,
                        true,
                        true,
                        false,
                        true,
                        false,
                        false,
                        true
                }),
                Map.entry("stringArray", new String[] {
                        "The last",
                        "array",
                        "totally",
                        "doesn't",
                        "mean",
                        "Hi",
                        "in",
                        "binary!"
                })
        );

        runTest(config, expectedOutput);
    }

    @Test
    public void parseNestedArraysWithBasicValues() throws OffsetConfigException {
        String config = """
                coolArrays:
                    integerArray = Tint [
                        1234
                        4321
                        6789
                        9876
                    ]
                    floatArray = Tfloat [
                        12.34
                        43.21
                        67.89
                        98.76
                    ]
                    iHaveABooleanArray:
                        booleanArray = Tboolean [
                            false
                            true
                            false
                            false
                            true
                            false
                            false
                            false
                            false
                            true
                            true
                            false
                            true
                            false
                            false
                            true
                        ]
                    stringArray = Tstring [
                        "The last"
                        "array"
                        "totally"
                        "doesn't"
                        "mean"
                        "Hi"
                        "in"
                        "binary!"
                    ]
                """;
        Map<String, Object> expectedOutput = Map.ofEntries(
                Map.entry("coolArrays.integerArray", new int[] {
                        1234,
                        4321,
                        6789,
                        9876
                }),
                Map.entry("coolArrays.floatArray", new float[] {
                        12.34f,
                        43.21f,
                        67.89f,
                        98.76f
                }),
                Map.entry("coolArrays.iHaveABooleanArray.booleanArray", new boolean[] {
                        false,
                        true,
                        false,
                        false,
                        true,
                        false,
                        false,
                        false,
                        false,
                        true,
                        true,
                        false,
                        true,
                        false,
                        false,
                        true
                }),
                Map.entry("coolArrays.stringArray", new String[] {
                        "The last",
                        "array",
                        "totally",
                        "doesn't",
                        "mean",
                        "Hi",
                        "in",
                        "binary!"
                })
        );

        runTest(config, expectedOutput);
    }

    @Test
    public void parseObjects() throws OffsetConfigException {
        String config = """
                veryCoolObject = Ttop.offsetmonkey538.offsetconfig538.exampleClasses.VeryCoolObject {
                    anInteger = 1234
                    aFloat = 12.34
                    aTrueBoolean = true
                    aFalseBoolean = false
                    aString = "Hello, World!"
                }
                anotherVeryCoolObject = Ttop.offsetmonkey538.offsetconfig538.exampleClasses.VeryCoolObject {
                    anInteger = 4321
                    aFloat = 43.21
                    aTrueBoolean = true
                    aFalseBoolean = false
                    aString = "Goodbye, World!"
                }
                """;
        Map<String, Object> expectedOutput = Map.ofEntries(
                Map.entry("veryCoolObject", new VeryCoolObject(
                        1234,
                        12.34f,
                        true,
                        false,
                        "Hello, World!"
                )),
                Map.entry("anotherVeryCoolObject", new VeryCoolObject(
                        4321,
                        43.21f,
                        true,
                        false,
                        "Goodbye, World!"
                ))
        );

        offsetConfig538.addSerializer(new VeryCoolObject.VeryCoolObjectSerializer());
        runTest(config, expectedOutput);
    }

    @Test
    public void parseNestedObjects() throws OffsetConfigException {
        String config = """
                veryCoolObjects:
                    veryCoolObject = Ttop.offsetmonkey538.offsetconfig538.exampleClasses.VeryCoolObject {
                        anInteger = 1234
                        aFloat = 12.34
                        aTrueBoolean = true
                        aFalseBoolean = false
                        aString = "Hello, World!"
                    }
                    anotherVeryCoolObject = Ttop.offsetmonkey538.offsetconfig538.exampleClasses.VeryCoolObject {
                        anInteger = 4321
                        aFloat = 43.21
                        aTrueBoolean = true
                        aFalseBoolean = false
                        aString = "Goodbye, World!"
                    }
                """;
        Map<String, Object> expectedOutput = Map.ofEntries(
                Map.entry("veryCoolObjects.veryCoolObject", new VeryCoolObject(
                        1234,
                        12.34f,
                        true,
                        false,
                        "Hello, World!"
                )),
                Map.entry("veryCoolObjects.anotherVeryCoolObject", new VeryCoolObject(
                        4321,
                        43.21f,
                        true,
                        false,
                        "Goodbye, World!"
                ))
        );

        offsetConfig538.addSerializer(new VeryCoolObject.VeryCoolObjectSerializer());
        runTest(config, expectedOutput);
    }

    @Test
    public void parseObjectWithArray() throws OffsetConfigException {
        String config = """
                veryCoolObject = Ttop.offsetmonkey538.offsetconfig538.exampleClasses.VeryCoolObjectWithArray {
                    firstArray = Tint [
                        1234
                        4321
                        6789
                        9876
                    ]
                    secondArray = Tstring [
                        "Hello, World!"
                        "Goodbye, World!"
                    ]
                }
                """;
        Map<String, Object> expectedOutput = Map.ofEntries(
                Map.entry("veryCoolObject", new VeryCoolObjectWithArray(
                        new int[] {
                                1234,
                                4321,
                                6789,
                                9876
                        },
                        new String[] {
                                "Hello, World!",
                                "Goodbye, World!"
                        }
                ))
        );

        offsetConfig538.addSerializer(new VeryCoolObjectWithArray.VeryCoolObjectWithArraySerializer());
        runTest(config, expectedOutput);
    }

    @Test
    public void parseObjectWithObjectWithArray() throws OffsetConfigException {
        String config = """
                veryCoolObject = Ttop.offsetmonkey538.offsetconfig538.exampleClasses.VeryCoolObjectWithObject {
                    anInteger = 1234
                    coolObject = Ttop.offsetmonkey538.offsetconfig538.exampleClasses.VeryCoolObjectWithArray {
                        firstArray = Tint [
                            1234
                            4321
                            6789
                            9876
                        ]
                        secondArray = Tstring [
                            "Hello, World!"
                            "Goodbye, World!"
                        ]
                    }
                }
                """;
        Map<String, Object> expectedOutput = Map.ofEntries(
                Map.entry("veryCoolObject", new VeryCoolObjectWithObject(
                        1234,
                        new VeryCoolObjectWithArray(
                                new int[] {
                                        1234,
                                        4321,
                                        6789,
                                        9876
                                },
                                new String[] {
                                        "Hello, World!",
                                        "Goodbye, World!"
                                }
                        )
                ))
        );

        offsetConfig538.addSerializer(new VeryCoolObjectWithObject.VeryCoolObjectSerializer());
        offsetConfig538.addSerializer(new VeryCoolObjectWithArray.VeryCoolObjectWithArraySerializer());

        runTest(config, expectedOutput);
    }

    @Test
    public void parseObjectArray() throws OffsetConfigException {
        String config = """
                myObjectArray = Ttop.offsetmonkey538.offsetconfig538.exampleClasses.VeryCoolObject [
                    {
                        anInteger = 1234
                        aFloat = 12.34
                        aTrueBoolean = true
                        aFalseBoolean = false
                        aString = "Hello, World!"
                    }
                    {
                        anInteger = 4321
                        aFloat = 43.21
                        aTrueBoolean = true
                        aFalseBoolean = false
                        aString = "Goodbye, World!"
                    }
                    {
                        anInteger = 2134
                        aFloat = 21.34
                        aTrueBoolean = true
                        aFalseBoolean = false
                        aString = "I'm the third object in this array!"
                    }
                ]
                """;
        Map<String, Object> expectedOutput = Map.ofEntries(
                Map.entry("myObjectArray", new VeryCoolObject[]{
                                new VeryCoolObject(
                                        1234,
                                        12.34f,
                                        true,
                                        false,
                                        "Hello, World!"
                                ),
                                new VeryCoolObject(
                                        4321,
                                        43.21f,
                                        true,
                                        false,
                                        "Goodbye, World!"
                                ),
                                new VeryCoolObject(
                                        2134,
                                        21.34f,
                                        true,
                                        false,
                                        "I'm the third object in this array!"
                                )
                        }
                )
        );

        offsetConfig538.addSerializer(new VeryCoolObject.VeryCoolObjectSerializer());
        runTest(config, expectedOutput);
    }

    @Test
    public void parseObjectWithArrayArray() throws OffsetConfigException {
        String config = """
                myObjectArray = Ttop.offsetmonkey538.offsetconfig538.exampleClasses.VeryCoolObjectWithArray [
                    {
                        firstArray = Tint [
                            1
                            2
                            3
                            4
                        ]
                        secondArray = Tstring [
                            "1234"
                            "4321"
                            "6789"
                            "9876"
                        ]
                    }
                    {
                        firstArray = Tint [
                            4
                            3
                            2
                            1
                        ]
                        secondArray = Tstring [
                            "9876"
                            "6789"
                            "4321"
                            "1234"
                        ]
                    }
                    {
                        firstArray = Tint [
                            6
                            7
                            8
                            9
                        ]
                        secondArray = Tstring [
                            "4321"
                            "1234"
                            "9876"
                            "6789"
                        ]
                    }
                ]
                """;
        Map<String, Object> expectedOutput = Map.ofEntries(
                Map.entry("myObjectArray", new VeryCoolObjectWithArray[]{
                                new VeryCoolObjectWithArray(
                                        new int[]{
                                                1,
                                                2,
                                                3,
                                                4
                                        },
                                        new String[]{
                                                "1234",
                                                "4321",
                                                "6789",
                                                "9876"
                                        }
                                ),
                                new VeryCoolObjectWithArray(
                                        new int[]{
                                                4,
                                                3,
                                                2,
                                                1
                                        },
                                        new String[]{
                                                "9876",
                                                "6789",
                                                "4321",
                                                "1234"
                                        }
                                ),
                                new VeryCoolObjectWithArray(
                                        new int[]{
                                                6,
                                                7,
                                                8,
                                                9
                                        },
                                        new String[]{
                                                "4321",
                                                "1234",
                                                "9876",
                                                "6789"
                                        }
                                )
                        }
                )
        );

        offsetConfig538.addSerializer(new VeryCoolObjectWithArray.VeryCoolObjectWithArraySerializer());
        runTest(config, expectedOutput);
    }



    private void runTestWithComments(String config, Map<String, ConfigEntry> expectedOutput) throws OffsetConfigException {
        Map<String, ConfigEntry> actualOutput = offsetConfig538.getParser().parse(config);

        for (Map.Entry<String, ConfigEntry> entry : expectedOutput.entrySet()) {
            String key = entry.getKey();
            Object expectedValue = entry.getValue();
            Object actualValue = actualOutput.get(key);

            if (expectedValue.getClass().isArray() && actualValue.getClass().isArray()) runTestOnArray(expectedValue, actualValue);
            else assertEquals(expectedValue, actualValue);
        }
    }

    private void runTest(String config, Map<String, Object> expectedOutput) throws OffsetConfigException {
        Map<String, Object> actualOutput = offsetConfig538.getParser().parseWithoutComments(config);

        for (Map.Entry<String, Object> entry : expectedOutput.entrySet()) {
            String key = entry.getKey();
            Object expectedValue = entry.getValue();
            Object actualValue = actualOutput.get(key);

            if (expectedValue.getClass().isArray() && actualValue.getClass().isArray()) runTestOnArray(expectedValue, actualValue);
            else assertEquals(expectedValue, actualValue);
        }
    }

    private void runTestOnArray(Object expectedValue, Object actualValue) {
        Object[] expectedArray = ArrayUtils.castTo(expectedValue, Object.class);

        assertArrayEquals(expectedArray, (Object[]) actualValue);
    }
}
