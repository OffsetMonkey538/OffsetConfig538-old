package top.offsetmonkey538.offsetconfig538.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ArrayUtilsTest {

    @Test
    public void castObjectToStringArray() {
        Object input = new String[] {
                "String 1",
                "String 2",
                "String 3",
                "String 4",
                "String 5"
        };
        String[] expected = new String[] {
                "String 1",
                "String 2",
                "String 3",
                "String 4",
                "String 5"
        };
        String[] actual = ArrayUtils.castTo(input, String.class);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void castObjectToIntArray() {
        Object input = new int[] {
                1,
                2,
                3,
                4,
                5
        };
        int[] expected = new int[] {
                1,
                2,
                3,
                4,
                5
        };
        int[] actual = ArrayUtils.castToInt(input);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void castObjectToFloatArray() {
        Object input = new float[] {
                1.2f,
                2.3f,
                3.4f,
                4.5f,
                5.6f
        };
        float[] expected = new float[] {
                1.2f,
                2.3f,
                3.4f,
                4.5f,
                5.6f
        };
        float[] actual = ArrayUtils.castToFloat(input);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void castObjectToBooleanArray() {
        Object input = new boolean[] {
                true,
                true,
                false,
                true,
                false,
                false
        };
        boolean[] expected = new boolean[] {
                true,
                true,
                false,
                true,
                false,
                false
        };
        boolean[] actual = ArrayUtils.castToBoolean(input);

        assertArrayEquals(expected, actual);
    }
}
