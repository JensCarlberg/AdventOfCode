package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec16 extends Christmas {

    static final Point LEFT  = new Point( 0, -1);
    static final Point RIGHT = new Point( 0,  1);
    static final Point UP    = new Point(-1,  0);
    static final Point DOWN  = new Point( 1,  0);

    long sampleAnswer1 = 46;
    long sampleAnswer2 = 51;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec16();
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

    private char[][] convertData(Stream<String> data) {
        var lines = data.toList();
        var map = new char[lines.size()][lines.get(0).length()];
        for (var row=0; row<map.length; row++)
            for (var col=0; col<map[row].length; col++)
                map[row][col] = lines.get(row).charAt(col);
        return map;
    }

    private void displayMap(char[][] map, Point pos) {
        for (int row=0; row<map.length; row++) {
            for (int col=0; col<map[row].length; col++)
                if (row == pos.row && col == pos.col)
                    System.out.print(highLight(map[row][col]));
                else System.out.print(map[row][col]);
            System.out.println();
        }
    }

    private char highLight(char c) {
        switch (c) {
        case '/': return '7';
        case '\\': return '4';
        case '|': return '‼';
        case '-': return '=';
        case '.': return '*';
        default: return c;
        }
    }

    public long solve1(Stream<String> stream) {
        var map = convertData(stream);
        var energizedMap = new TreeMap<Point, List<Point>>();
        var pos = new Point(0, 0);
        var dir = RIGHT;
        beamAway(map, energizedMap, pos, dir);
        return energizedMap.size();
    }

    private void beamAway(char[][] map, TreeMap<Point, List<Point>> energizedMap, Point pos, Point dir) {
        if (pos.row < 0 || pos.col < 0 || pos.row >= map.length || pos.col >= map[pos.row].length)
            return; // Outside the map, hits the walls
        var beams = getBeamsForPos(pos, energizedMap);
        if (beams.contains(dir))
            return; // Been here, done that
        beams.add(dir);
        energizedMap.put(pos, beams);

//        displayMap(map, pos);
        char terrain = map[pos.row][pos.col];
        switch (terrain) {
        case '/':
            dir = reflectRightUp(dir);
            beamAway(map, energizedMap, pos.add(dir), dir);
            return;
        case '\\':
            dir = reflectRightDown(dir);
            beamAway(map, energizedMap, pos.add(dir), dir);
            return;
        case '-':
            var leftRight = splitRightLeft(dir);
            for (var newDir: leftRight)
                beamAway(map, energizedMap, pos.add(newDir), newDir);
            return;
        case '|':
            var upDown = splitUpDown(dir);
            for (var newDir: upDown)
                beamAway(map, energizedMap, pos.add(newDir), newDir);
            return;
        default:
            beamAway(map, energizedMap, pos.add(dir), dir);
        }
    }

    private List<Point> splitRightLeft(Point dir) {
        var splitDirs = new ArrayList<Point>();
        if (dir == LEFT || dir == RIGHT) {
            splitDirs.add(dir);
            return splitDirs;
        }
        splitDirs.add(LEFT);
        splitDirs.add(RIGHT);
        return splitDirs;
    }

    private List<Point> splitUpDown(Point dir) {
        var splitDirs = new ArrayList<Point>();
        if (dir == UP || dir == DOWN) {
            splitDirs.add(dir);
            return splitDirs;
        }
        splitDirs.add(UP);
        splitDirs.add(DOWN);
        return splitDirs;
    }

    private Point reflectRightUp(Point dir) {
        if (dir == LEFT)  return DOWN;
        if (dir == RIGHT) return UP;
        if (dir == UP)    return RIGHT;
        if (dir == DOWN)  return LEFT;

        throw new RuntimeException("Unsupported dir");
    }

    private Point reflectRightDown(Point dir) {
        if (dir == LEFT)  return UP;
        if (dir == RIGHT) return DOWN;
        if (dir == UP)    return LEFT;
        if (dir == DOWN)  return RIGHT;

        throw new RuntimeException("Unsupported dir");
    }

    private List<Point> getBeamsForPos(Point pos, TreeMap<Point, List<Point>> map) {
        if (map.containsKey(pos))
            return map.get(pos);
        return new ArrayList<>();
    }

    public long solve2(Stream<String> stream) {
        var map = convertData(stream);
        int maxEnergized = Integer.MIN_VALUE;
        for (var row = 0; row<map.length; row++) {
            var pos = new Point(row, 0);
            maxEnergized = max(maxEnergized, findEnergizedCount(map, pos, RIGHT));
        }
        for (var row = 0; row<map.length; row++) {
            var pos = new Point(row, map[row].length - 1);
            maxEnergized = max(maxEnergized, findEnergizedCount(map, pos, LEFT));
        }
        for (var col = 0; col<map[0].length; col++) {
            var pos = new Point(0, col);
            maxEnergized = max(maxEnergized, findEnergizedCount(map, pos, DOWN));
        }
        for (var col = 0; col<map[0].length; col++) {
            var pos = new Point(map.length - 1, col);
            maxEnergized = max(maxEnergized, findEnergizedCount(map, pos, UP));
        }

        return maxEnergized;
    }

    private int max(int left, int right) {
        if (left > right) return left;
        return right;
    }

    private int findEnergizedCount(char[][] map, Point pos, Point dir) {
        var energizedMap = new TreeMap<Point, List<Point>>();
        beamAway(map, energizedMap, pos, dir);
        int energizedCount = energizedMap.size();
        return energizedCount;
    }

    record Point(int row, int col) implements Comparable<Point> {
        Point add (Point p) { return new Point(row + p.row, col + p.col); }
        @Override public int compareTo(Point o) {
            return Objects.compare(this, o,
                    Comparator.comparing(Point::row)
                    .thenComparing(Point::col));
        }
    }

    record Energized(List<Point> beamDirs) {}
}
