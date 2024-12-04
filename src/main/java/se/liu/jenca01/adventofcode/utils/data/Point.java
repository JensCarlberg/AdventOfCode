package se.liu.jenca01.adventofcode.utils.data;

import java.util.Comparator;
import java.util.Objects;

public record Point(int x, int y) implements Comparable<Point> {
    @Override public int compareTo(Point o) {
        return Objects.compare(this, o, Comparator.comparing(Point::x).thenComparing(Point::y));
    }

    public Point add(Point o) {
        return new Point(this.x + o.x, this.y + o.y);
    }
}
