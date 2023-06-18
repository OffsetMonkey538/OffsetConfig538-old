package top.offsetmonkey538.offsetconfig538.generation;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import top.offsetmonkey538.offsetconfig538.OffsetConfig538;
import top.offsetmonkey538.offsetconfig538.exampleClasses.VeryCoolObject;
import top.offsetmonkey538.offsetconfig538.exampleClasses.VeryCoolObjectWithArray;
import top.offsetmonkey538.offsetconfig538.exampleClasses.VeryCoolObjectWithObject;
import top.offsetmonkey538.offsetconfig538.exception.OffsetConfigException;
import top.offsetmonkey538.offsetconfig538.ConfigEntryWithComment;

import static org.junit.jupiter.api.Assertions.*;

public class GeneratorTest {
    private static final OffsetConfig538 offsetConfig538 = new OffsetConfig538();

    @Test
    public void generateBasicValues() throws OffsetConfigException {
        String expectedOutput = """
                anInteger = 1234
                aFloat = 12.34
                aTrueBoolean = true
                aFalseBoolean = false
                aString = "Hello, World!"
                """;
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("anInteger", 1234);
        input.put("aFloat", 12.34f);
        input.put("aTrueBoolean", true);
        input.put("aFalseBoolean", false);
        input.put("aString", "Hello, World!");

        runTest(input, expectedOutput);
    }

    @Test
    public void generateBasicValuesWithComments() throws OffsetConfigException {
        String expectedOutput = """
                # An integer value
                anInteger = 1234
                # A float value
                aFloat = 12.34
                # A boolean value that's true
                aTrueBoolean = true
                # A boolean value that's false
                aFalseBoolean = false
                # A string value
                aString = "Hello, World!"
                """;
        Map<String, ConfigEntryWithComment> input = new LinkedHashMap<>();
        input.put("anInteger", new ConfigEntryWithComment("An integer value", 1234));
        input.put("aFloat", new ConfigEntryWithComment("A float value", 12.34f));
        input.put("aTrueBoolean", new ConfigEntryWithComment("A boolean value that's true", true));
        input.put("aFalseBoolean", new ConfigEntryWithComment("A boolean value that's false", false));
        input.put("aString", new ConfigEntryWithComment("A string value", "Hello, World!"));

        runTestWithComments(input, expectedOutput);
    }

    @Test
    public void generateNestedBasicValues() throws OffsetConfigException {
        String expectedOutput = """
                iHaveAnIntegerAndAFloat:
                    anInteger = 1234
                    aFloat = 12.34
                aTrueBoolean = true
                iHaveABooleanAndAString:
                    aFalseBoolean = false
                    aString = "Hello, World!"
                """;
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("iHaveAnIntegerAndAFloat.anInteger", 1234);
        input.put("iHaveAnIntegerAndAFloat.aFloat", 12.34f);
        input.put("aTrueBoolean", true);
        input.put("iHaveABooleanAndAString.aFalseBoolean", false);
        input.put("iHaveABooleanAndAString.aString", "Hello, World!");

        runTest(input, expectedOutput);
    }

    @Test
    public void generateNestedBasicValuesWithComments() throws OffsetConfigException {
        String expectedOutput = """
                # A block containing an integer and a float
                iHaveAnIntegerAndAFloat:
                    # An integer value
                    anInteger = 1234
                    # A float value
                    aFloat = 12.34
                # A boolean value that's true
                aTrueBoolean = true
                # A block containing a boolean and a string
                iHaveABooleanAndAString:
                    # A boolean value that's false
                    aFalseBoolean = false
                    # A string value
                    aString = "Hello, World!"
                """;
        Map<String, ConfigEntryWithComment> input = new LinkedHashMap<>();
        input.put("iHaveAnIntegerAndAFloat", new ConfigEntryWithComment("A block containing an integer and a float"));
        input.put("iHaveAnIntegerAndAFloat.anInteger", new ConfigEntryWithComment("An integer value", 1234));
        input.put("iHaveAnIntegerAndAFloat.aFloat", new ConfigEntryWithComment("A float value", 12.34f));
        input.put("aTrueBoolean", new ConfigEntryWithComment("A boolean value that's true", true));
        input.put("iHaveABooleanAndAString", new ConfigEntryWithComment("A block containing a boolean and a string"));
        input.put("iHaveABooleanAndAString.aFalseBoolean", new ConfigEntryWithComment("A boolean value that's false", false));
        input.put("iHaveABooleanAndAString.aString", new ConfigEntryWithComment("A string value", "Hello, World!"));

        runTestWithComments(input, expectedOutput);
    }

    @Test
    public void generateMultiNestedBasicValues() throws OffsetConfigException {
        String expectedOutput = """
                iHaveAnIntegerAndANestedFloat:
                    anInteger = 1234
                    iHaveAFloat:
                        aFloat = 12.34
                aTrueBoolean = true
                iHaveABooleanAndAString:
                    aFalseBoolean = false
                    aString = "Hello, World!"
                """;
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("iHaveAnIntegerAndANestedFloat.anInteger", 1234);
        input.put("iHaveAnIntegerAndANestedFloat.iHaveAFloat.aFloat", 12.34f);
        input.put("aTrueBoolean", true);
        input.put("iHaveABooleanAndAString.aFalseBoolean", false);
        input.put("iHaveABooleanAndAString.aString", "Hello, World!");

        runTest(input, expectedOutput);
    }

    @Test
    public void generateArraysWithBasicValues() throws OffsetConfigException {
        String expectedOutput = """
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
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("integerArray", new int[]{
                1234,
                4321,
                6789,
                9876
        });
        input.put("floatArray", new float[]{
                12.34f,
                43.21f,
                67.89f,
                98.76f
        });
        input.put("booleanArray", new boolean[]{
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
        });
        input.put("stringArray", new String[]{
                "The last",
                "array",
                "totally",
                "doesn't",
                "mean",
                "Hi",
                "in",
                "binary!"
        });

        runTest(input, expectedOutput);
    }

    @Test
    public void generateNestedArraysWithBasicValues() throws OffsetConfigException {
        String expectedOutput = """
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
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("coolArrays.integerArray", new int[] {
                1234,
                4321,
                6789,
                9876
        });
        input.put("coolArrays.floatArray", new float[] {
                12.34f,
                43.21f,
                67.89f,
                98.76f
        });
        input.put("coolArrays.iHaveABooleanArray.booleanArray", new boolean[] {
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
        });
        input.put("coolArrays.stringArray", new String[] {
                "The last",
                "array",
                "totally",
                "doesn't",
                "mean",
                "Hi",
                "in",
                "binary!"
        });

        runTest(input, expectedOutput);
    }

    @Test
    public void generateObjects() throws OffsetConfigException {
        String expectedOutput = """
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
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("veryCoolObject", new VeryCoolObject(
                1234,
                12.34f,
                true,
                false,
                "Hello, World!"
        ));
        input.put("anotherVeryCoolObject", new VeryCoolObject(
                4321,
                43.21f,
                true,
                false,
                "Goodbye, World!"
        ));

        offsetConfig538.addSerializer(new VeryCoolObject.VeryCoolObjectSerializer());
        runTest(input, expectedOutput);
    }

    @Test
    public void generateNestedObjects() throws OffsetConfigException {
        String expectedOutput = """
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
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("veryCoolObjects.veryCoolObject", new VeryCoolObject(
                1234,
                12.34f,
                true,
                false,
                "Hello, World!"
        ));
        input.put("veryCoolObjects.anotherVeryCoolObject", new VeryCoolObject(
                4321,
                43.21f,
                true,
                false,
                "Goodbye, World!"
        ));

        offsetConfig538.addSerializer(new VeryCoolObject.VeryCoolObjectSerializer());
        runTest(input, expectedOutput);
    }

    @Test
    public void generateObjectWithArray() throws OffsetConfigException {
        String expectedOutput = """
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
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("veryCoolObject", new VeryCoolObjectWithArray(
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
        ));

        offsetConfig538.addSerializer(new VeryCoolObjectWithArray.VeryCoolObjectWithArraySerializer());
        runTest(input, expectedOutput);
    }

    @Test
    public void generateObjectArray() throws OffsetConfigException {
        String expectedOutput = """
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
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("myObjectArray", new VeryCoolObject[]{
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
        );

        offsetConfig538.addSerializer(new VeryCoolObject.VeryCoolObjectSerializer());
        runTest(input, expectedOutput);
    }

    @Test
    public void generateObjectWithArrayArray() throws OffsetConfigException {
        String expectedOutput = """
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
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("myObjectArray", new VeryCoolObjectWithArray[]{
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
        );

        offsetConfig538.addSerializer(new VeryCoolObjectWithArray.VeryCoolObjectWithArraySerializer());
        runTest(input, expectedOutput);
    }

    @Test
    public void parseObjectWithObjectWithArray() throws OffsetConfigException {
        String expectedOutput = """
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
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("veryCoolObject", new VeryCoolObjectWithObject(
                1234,
                new VeryCoolObjectWithArray(
                        new int[]{
                                1234,
                                4321,
                                6789,
                                9876
                        },
                        new String[]{
                                "Hello, World!",
                                "Goodbye, World!"
                        }
                )
        ));

        offsetConfig538.addSerializer(new VeryCoolObjectWithObject.VeryCoolObjectSerializer());
        offsetConfig538.addSerializer(new VeryCoolObjectWithArray.VeryCoolObjectWithArraySerializer());

        runTest(input, expectedOutput);
    }



    private void runTest(Map<String, Object> input, String expectedOutput) throws OffsetConfigException {
        String actualOutput = offsetConfig538.getGenerator().generateFromObjects(input);

        assertEquals(expectedOutput, actualOutput);
    }
    private void runTestWithComments(Map<String, ConfigEntryWithComment> input, String expectedOutput) throws OffsetConfigException {
        String actualOutput = offsetConfig538.getGenerator().generateFromConfigEntries(input);

        assertEquals(expectedOutput, actualOutput);
    }
}
