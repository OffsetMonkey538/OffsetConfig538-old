package top.offsetmonkey538.offsetconfig538.parsing;

import top.offsetmonkey538.offsetconfig538.exception.OffsetConfigException;
import top.offsetmonkey538.offsetconfig538.serialization.OffsetConfigSerializer;
import top.offsetmonkey538.offsetconfig538.util.ArrayUtils;

import java.util.Arrays;
import java.util.Map;

public record VeryCoolObjectWithArray(int[] firstArray, String[] secondArray) {

    public static class VeryCoolObjectWithArraySerializer implements OffsetConfigSerializer<VeryCoolObjectWithArray> {
        @Override
        public VeryCoolObjectWithArray deserialize(Map<String, Object> entries) throws OffsetConfigException {
            return new VeryCoolObjectWithArray(
                    ArrayUtils.castToInt(entries.get("firstArray")),
                    ArrayUtils.castTo(entries.get("secondArray"), String.class)
            );
        }
    }

    // Just so the test actually compares the objects correctly.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VeryCoolObjectWithArray that = (VeryCoolObjectWithArray) o;

        if (!Arrays.equals(firstArray, that.firstArray)) return false;
        return Arrays.equals(secondArray, that.secondArray);
    }
}
