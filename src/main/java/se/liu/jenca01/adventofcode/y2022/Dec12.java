package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec12 extends Christmas {

    private static final String NO_ROUTE_FOUND = "Cannot find route from start to end";
    long sampleAnswer1 = 31;
    long sampleAnswer2 = 29;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec12();
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

    private Puzzle convertData(Stream<String> data) {
        var lines = data.toList();
        var heights = new char[lines.size()][lines.get(0).length()];
        var startRow = 0;
        var startCol = 0;
        var endRow = 0;
        var endCol = 0;
        for(var row=0; row<heights.length; row++)
            for (var col=0; col<heights[0].length; col++) {
                var height = lines.get(row).charAt(col);
                if (height == 'S') {
                    heights[row][col] = 'a';
                    startRow = row;
                    startCol = col;
                } else if (height == 'E') {
                    heights[row][col] = 'z';
                    endRow = row;
                    endCol = col;
                } else {
                    heights[row][col] = height;
                }
            }
        return new Puzzle(heights, new Pos(startRow, startCol), new Pos(endRow, endCol));
    }

    public long solve1(Stream<String> stream) {
        var puzzle = convertData(stream);
        var start = puzzle.start;
        var map = puzzle.map;
        var end = puzzle.end;
        return solve(start, map, end);
    }

    private long solve(Pos start, char[][] map, Pos end) {
        var stepsTo = new int[map.length][map[0].length];
        var visited = new TreeSet<Pos>();
        var step = 1;
        stepsTo[start.row][start.col] = step++;
        visited.add(start);
        while (true) {
            var loopPos = new TreeSet<Pos>();
            loopPos.addAll(visited);
            for (var pos: loopPos)
                addVisited(pos, step, visited, stepsTo, map, end);
            if (loopPos.size() == visited.size())
                throw new RuntimeException(NO_ROUTE_FOUND);
            if (visited.contains(end))
                return --step;
            step++;
        }
    }

    private void addVisited(Pos pos, int step, Set<Pos> visited, int[][] stepsTo, char[][] map, Pos end) {
        var height = map[pos.row][pos.col];
        Pos left = new Pos(pos.row - 1, pos.col);
        Pos right = new Pos(pos.row + 1, pos.col);
        Pos up = new Pos(pos.row, pos.col - 1);
        Pos down = new Pos(pos.row, pos.col + 1);
        handlePos(step, visited, stepsTo, map, height, left);
        handlePos(step, visited, stepsTo, map, height, right);
        handlePos(step, visited, stepsTo, map, height, up);
        handlePos(step, visited, stepsTo, map, height, down);
    }

    private void handlePos(int step, Set<Pos> visited, int[][] stepsTo, char[][] map, char height, Pos pos) {
        if (posExist(pos, map) && notToSteep(pos, height, map) && !visited.contains(pos)) {
            stepsTo[pos.row][pos.col] = step;
            visited.add(pos);
        }
    }

    private boolean posExist(Pos pos, char[][] map) {
        if (pos.row < 0 || pos.col < 0) return false;
        if (pos.row >= map.length || pos.col >= map[0].length) return false;
        return true;
    }

    private boolean notToSteep(Pos left, char height, char[][] map) {
        var newHeight = map[left.row][left.col];
        var diff = newHeight - height;
        return diff < 2;
    }

    public long solve2(Stream<String> stream) {
        var puzzle = convertData(stream);
        var map = puzzle.map;
        var end = puzzle.end;
        var minSteps = Long.MAX_VALUE;
        for(var row=0; row<map.length; row++) {
            System.out.print(".");
            for (var col=0; col<map[0].length; col++) {
                if (map[row][col] == 'a') try {
                    minSteps = min(minSteps, solve(new Pos(row, col), map, end));
                } catch (RuntimeException re) {
                    if (NO_ROUTE_FOUND.equals(re.getMessage()))
                        continue;
                    throw re;
                }
            }
        }
        return minSteps;
    }

    private long min(long a, long b) {
        if (a <= b) return a;
        System.out.println(b);
        return b;
    }

    record Pos(int row, int col) implements Comparable<Pos> {
        @Override public int compareTo(Pos o) {
            return Objects.compare(this, o, Comparator.comparing(Pos::row).thenComparing(Pos::col));
        }
    }
    record Puzzle(char[][] map, Pos start, Pos end) {}
}

/*



 */