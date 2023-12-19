package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec17 extends Christmas {

    long sampleAnswer1 = 102;
    long sampleAnswer2 = 0;

    static final Point LEFT  = new Point( 0, -1);
    static final Point RIGHT = new Point( 0,  1);
    static final Point UP    = new Point(-1,  0);
    static final Point DOWN  = new Point( 1,  0);

    public static void main(String[] args) throws Exception {
        var christmas = new Dec17();
        christmas.solveSample();
        christmas.solveMy();
    }

    @Override
    public void solveSample() throws Exception {
        long start = System.currentTimeMillis();
        long solve1 = solve1(sampleData());
        long stop = System.currentTimeMillis();
        System.out.println("Example solve1 timing: " + (stop-start) + "ms");
        assertEquals(sampleAnswer1, solve1, "solve1 is expected to return " + sampleAnswer1);

        start = System.currentTimeMillis();
        long solve2 = solve2(sampleData());
        stop = System.currentTimeMillis();
        System.out.println("Example solve2 timing: " + (stop-start) + "ms");
        assertEquals(sampleAnswer2, solve2, "solve2 is expected to return " + sampleAnswer2);
    }

    @Override
    public void solveMy() throws Exception {
        System.out.println(simpleClassName() + " solve1: " + solve1(myData()));
        System.out.println(simpleClassName() + " solve2: " + solve2(myData()));
    }

    private byte[][] convertData(Stream<String> data) {
        var lines = data.toList();
        var map = new byte[lines.size()][lines.get(0).length()];
        for (int row=0; row<map.length; row++)
            for (int col = 0; col < map[0].length; col++)
                map[row][col] = (byte) (lines.get(row).charAt(col) - '0');
        return map;
    }

    public long solve1(Stream<String> stream) {
        var map = convertData(stream);
        var start = new Point(0, 0);
        var end = new Point(map.length-1, map[0].length-1);
//        var heatLossMap = new TreeMap<Point, Pair<Integer, R>>
        return 0;
    }

    public long solve2(Stream<String> stream) {
        return 0;
    }

    record Point(int row, int col) implements Comparable<Point> {
        Point add (Point p) { return new Point(row + p.row, col + p.col); }
        @Override public int compareTo(Point o) {
            return Objects.compare(this, o,
                    Comparator.comparing(Point::row)
                    .thenComparing(Point::col));
        }
    }

    record HeatMapPoint(Point point, int heat, List<Point> reachable) {}
}
