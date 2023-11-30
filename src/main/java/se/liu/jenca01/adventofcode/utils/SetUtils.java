package se.liu.jenca01.adventofcode.utils;

import java.util.Set;
import java.util.TreeSet;

public class SetUtils {

    public static <E> boolean equals(Set<E> s1, Set<E> s2) {
        if (s1.size() != s2.size()) return false;
        var tmp1 = new TreeSet<E>();
        tmp1.addAll(s1);
        tmp1.removeAll(s2);
        if (tmp1.size() != 0) return false;
        var tmp2 = new TreeSet<E>();
        tmp2.addAll(s2);
        tmp2.removeAll(s1);
        return tmp2.size() == 0;
    }
}
