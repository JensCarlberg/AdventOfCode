package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec18 extends Christmas {

    long sampleAnswer1 = 62;
    long sampleAnswer2 = 952408144115L;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec18();
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

    private List<DigStep> convertData(Stream<String> data) {
        var lines = data.toList();
        var steps = new ArrayList<DigStep>();
        for (var line: lines)
            steps.add(parseLine(line));
        return steps;
    }

    void displayMap(Collection<Point> map) {
        var maxRow = map.stream().mapToInt(p -> p.row).max().orElseThrow();
        var minRow = map.stream().mapToInt(p -> p.row).min().orElseThrow();
        var maxCol = map.stream().mapToInt(p -> p.col).max().orElseThrow();
        var minCol = map.stream().mapToInt(p -> p.col).min().orElseThrow();

        for (int row = minRow; row <= maxRow; row++) {
            for (int col = minCol; col <= maxCol; col++)
                System.out.print(map.contains(new Point(row, col)) ? "#" : ".");
            System.out.println();
        }
        System.out.println();
    }

    private DigStep parseLine(String line) {
        var parts = line.split(" ");
        var dir = parseDir(parts[0].strip());
        var steps = Integer.parseInt(parts[1]);
        var colour = parts[2];
        return new DigStep(dir, steps, colour);
    }

    private Point parseDir(String dir) {
        switch (dir) {
        case "U": return Point.U;
        case "D": return Point.D;
        case "L": return Point.L;
        case "R": return Point.R;
        default: throw new IllegalArgumentException("Unexpected value: " + dir);
        }
    }

    public long solve1(Stream<String> stream) {
        var plan = convertData(stream);
        var start = new Point(0,0);
        int turns = 0;
        var currentPos = start;
        Point currentDir = null;
        var digged = new TreeSet<Point>();
        for (var step: plan) {
            turns += turnDir(currentDir, step.dir);
            currentDir = step.dir;
            for (int s=0; s<step.steps; s++) {
                currentPos = currentPos.add(step.dir);
                digged.add(currentPos);
            }
        }

        displayMap(digged);

        fillFor: for (var step: plan) {
            for (int s=0; s<step.steps; s++) {
                currentPos = currentPos.add(step.dir);
                Point lookToTheSide = lookToTheSide(currentPos, step.dir, turns > 0);
                if (!digged.contains(lookToTheSide)) {
                    fill(lookToTheSide, digged);
                    break fillFor;
                }
            }
        }

        return digged.size();
    }

    private void fill(Point from, Collection<Point> digged) {
        digged.add(from);
        var filledHere = new TreeSet<Point>();
        filledHere.add(from);

        while (true) {
            var fillCandidates = new ArrayList<Point>();
            for (var f: filledHere) {
                fillCandidates.add(f.add(Point.U));
                fillCandidates.add(f.add(Point.D));
                fillCandidates.add(f.add(Point.L));
                fillCandidates.add(f.add(Point.R));
            }
            filledHere.clear();
            for (var p: fillCandidates)
                if (!digged.contains(p)) {
                    digged.add(p);
                    filledHere.add(p);
                }

            if (filledHere.size() == 0)
                break;
//            displayMap(digged);
        }
    }

    private int turnDir(Point currentDir, Point dir) {
        if (currentDir == null) return 0;
        if (currentDir == Point.U && dir == Point.L) return -1;
        if (currentDir == Point.D && dir == Point.R) return -1;
        if (currentDir == Point.L && dir == Point.D) return -1;
        if (currentDir == Point.R && dir == Point.U) return -1;
        if (currentDir == Point.U && dir == Point.R) return 1;
        if (currentDir == Point.D && dir == Point.L) return 1;
        if (currentDir == Point.L && dir == Point.U) return 1;
        if (currentDir == Point.R && dir == Point.D) return 1;
        throw new RuntimeException("Wrong turn");
    }

    private Point lookToTheSide(Point pos, Point dir, boolean clockwise) {
        var lookDir = lookDir(dir, clockwise);
        return pos.add(lookDir);
    }

    private Point lookDir(Point dir, boolean clockwise) {
        if (dir == Point.U && clockwise) return Point.R;
        if (dir == Point.D && clockwise) return Point.L;
        if (dir == Point.L && clockwise) return Point.U;
        if (dir == Point.R && clockwise) return Point.D;
        if (dir == Point.U && !clockwise) return Point.L;
        if (dir == Point.D && !clockwise) return Point.R;
        if (dir == Point.L && !clockwise) return Point.D;
        if (dir == Point.R && !clockwise) return Point.U;
        throw new RuntimeException("Wrong turn");
    }

    public long solve2(Stream<String> stream) {
        return 0;
    }

    record Point(int row, int col) implements Comparable<Point> {
        public final static Point N = new Point(-1,  0);
        public final static Point S = new Point( 1,  0);
        public final static Point W = new Point( 0, -1);
        public final static Point E = new Point( 0,  1);
        public final static Point U = N;
        public final static Point D = S;
        public final static Point L = W;
        public final static Point R = E;
        Point add (Point p) { return new Point(row + p.row, col + p.col); }
        @Override public int compareTo(Point o) {
            return Objects.compare(this, o,
                    Comparator.comparing(Point::row)
                    .thenComparing(Point::col));
        }
    }

    record DigStep(Point dir, int steps, String colour) {
        Point colourDir() {
            switch (colour.charAt(colour.length() - 2)) {
            case '0': return Point.R;
            case '1': return Point.D;
            case '2': return Point.L;
            case '3': return Point.U;
            default: throw new RuntimeException();
            }
        }
        long colourSteps() {
            return Long.parseLong(colour.substring(2, colour.length() - 2), 16);
        }
    }

    record Puzzle(byte[][] map, long rowOffset, long colOffset, List<DigStep> diggingSteps) {}
}
