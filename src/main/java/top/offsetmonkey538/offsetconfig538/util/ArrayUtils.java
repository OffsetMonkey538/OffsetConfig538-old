package top.offsetmonkey538.offsetconfig538.util;

import java.lang.reflect.Array;

/**
 * Provides methods to cast array Objects into a specific array.
 */
public final class ArrayUtils {
    private ArrayUtils() {

    }

    /**
     * Casts the provided arrayObject into an array of type elementType.
     *
     * @param arrayObject The array as an object.
     * @param elementType The type to convert the array to.
     * @param <E> the type to convert the array to.
     * @return The arrayObject cast to an array of E.
     * @throws IllegalArgumentException When the provided arrayObject isn't an array.
     */
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

    /**
     * Casts the provided arrayObject into an array of int.
     * <br>
     * First uses {@link #castTo(Object, Class)} to cast the array to an array of Integers and then converts that to an array of ints.
     *
     * @param arrayObject The array as an object.
     * @return The arrayObject cast to an array of int.
     * @throws IllegalArgumentException When the provided arrayObject isn't an array.
     * @see #castTo(Object, Class)
     */
    public static int[] castToInt(Object arrayObject) {
        Integer[] wrapperArray = castTo(arrayObject, Integer.class);
        int[] primitiveArray = new int[wrapperArray.length];

        for (int i = 0; i < primitiveArray.length; i++) {
            primitiveArray[i] = wrapperArray[i];
        }

        return primitiveArray;
    }

    /**
     * Casts the provided arrayObject into an array of float.
     * <br>
     * First uses {@link #castTo(Object, Class)} to cast the array to an array of Floats and then converts that to an array of floats.
     *
     * @param arrayObject The array as an object.
     * @return The arrayObject cast to an array of float.
     * @throws IllegalArgumentException When the provided arrayObject isn't an array.
     * @see #castTo(Object, Class)
     */
    public static float[] castToFloat(Object arrayObject) {
        Float[] wrapperArray = castTo(arrayObject, Float.class);
        float[] primitiveArray = new float[wrapperArray.length];

        for (int i = 0; i < primitiveArray.length; i++) {
            primitiveArray[i] = wrapperArray[i];
        }

        return primitiveArray;
    }

    /**
     * Casts the provided arrayObject into an array of boolean.
     * <br>
     * First uses {@link #castTo(Object, Class)} to cast the array to an array of Booleans and then converts that to an array of booleans.
     *
     * @param arrayObject The array as an object.
     * @return The arrayObject cast to an array of boolean.
     * @throws IllegalArgumentException When the provided arrayObject isn't an array.
     * @see #castTo(Object, Class)
     */
    public static boolean[] castToBoolean(Object arrayObject) {
        Boolean[] wrapperArray = castTo(arrayObject, Boolean.class);
        boolean[] primitiveArray = new boolean[wrapperArray.length];

        for (int i = 0; i < primitiveArray.length; i++) {
            primitiveArray[i] = wrapperArray[i];
        }

        return primitiveArray;
    }
}
