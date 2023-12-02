package se.liu.jenca01.adventofcode.utils;

import java.util.Arrays;
import java.util.Collections;

public class ArrayUtils {
    public static <T> T[] reverse(T a[])
    {
        Collections.reverse(Arrays.asList(a));
        return a;
    }
}
