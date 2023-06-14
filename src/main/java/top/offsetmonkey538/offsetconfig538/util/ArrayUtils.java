package top.offsetmonkey538.offsetconfig538.util;

import java.lang.reflect.Array;

public final class ArrayUtils {
    private ArrayUtils() {

    }

    @SuppressWarnings("unchecked")
    public static <E> E[] castTo(Object arrayObject, Class<E> elementType) {
        Class<?> arrayClass = arrayObject.getClass();

        if (!arrayClass.isArray()) throw new IllegalArgumentException(String.format("Provided object '%s' not an array!", arrayObject));

        Class<?> componentType = arrayClass.getComponentType();
        if (elementType.isAssignableFrom(componentType)) return (E[]) arrayObject;

        int length = Array.getLength(arrayObject);
        E[] result = (E[]) Array.newInstance(elementType, length);

        for (int i = 0; i < length; i++) {
            result[i] = (E) Array.get(arrayObject, i);
        }

        return result;
    }

    public static int[] castToInt(Object arrayObject) {
        Integer[] wrapperArray = castTo(arrayObject, Integer.class);
        int[] primitiveArray = new int[wrapperArray.length];

        for (int i = 0; i < primitiveArray.length; i++) {
            primitiveArray[i] = wrapperArray[i];
        }

        return primitiveArray;
    }

    public static float[] castToFloat(Object arrayObject) {
        Float[] wrapperArray = castTo(arrayObject, Float.class);
        float[] primitiveArray = new float[wrapperArray.length];

        for (int i = 0; i < primitiveArray.length; i++) {
            primitiveArray[i] = wrapperArray[i];
        }

        return primitiveArray;
    }

    public static boolean[] castToBoolean(Object arrayObject) {
        Boolean[] wrapperArray = castTo(arrayObject, Boolean.class);
        boolean[] primitiveArray = new boolean[wrapperArray.length];

        for (int i = 0; i < primitiveArray.length; i++) {
            primitiveArray[i] = wrapperArray[i];
        }

        return primitiveArray;
    }
}
