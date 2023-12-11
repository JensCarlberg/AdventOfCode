package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec10 extends Christmas {

    final static Point N = new Point(0, -1);
    final static Point S = new Point(0, 1);
    final static Point E = new Point(1, 0);
    final static Point W = new Point(-1, 0);
    final static Point G = new Point(0, 0);

    enum Connection {
        NS('|', "│", N, S, 0),
        EW('-', "─", E, W, 0),
        NE('L', "└", N, E, -1),
        NW('J', "┘", N, W, 1),
        SW('7', "┐", S, W, -1),
        SE('F', "┌", S, E, 1),
        START('S', "■", G, G, 0),
        GROUND('.', " ", G, G, 0);

        final private char symbol;
        final private String presentation;
        final private Point d1;
        final private Point d2;
        final private int turn;
        Connection(char symbol, String presentation, Point d1, Point d2, int turn) {
            this.symbol = symbol;
            this.presentation = presentation;
            this.d1 = d1;
            this.d2 = d2;
            this.turn = turn;
        }
        static Connection parse(char s) {
            for (var conn: values())
                if (conn.symbol == s) return conn;
            throw new RuntimeException("Not found: " + s);
        }
        Pair<Point, Point> neighbours(Point point) {
            return Pair.of(point.add(d1), point.add(d2));
        }

    }
    long sampleAnswer1 = 8;
    long sampleAnswer2 = 0;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec10();
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

    private Connection[][] convertData(Stream<String> data) {
        var lines = data.toList();
        var map = new Connection[lines.size() + 2][lines.get(0).length() + 2];
        for(int col=0; col<map[0].length; col++)
            map[0][col] = Connection.GROUND;
        for (int row=1; row<map.length-1; row++) {
            map[row][0] = Connection.GROUND;
            var line = lines.get(row-1);
            for (int col=1; col<map[row].length-1; col++)
                map[row][col] = Connection.parse(line.charAt(col-1));
            map[row][map[row].length-1] = Connection.GROUND;
        }
        for(int col=0; col<map[0].length; col++)
            map[lines.size() + 1][col] = Connection.GROUND;
        return map;
    }

    private void displayMap(Connection[][]  map) {
        displayMap(map, new Point(-1, -1));
    }
    private void displayMap(Connection[][]  map, Point pos) {
        var row = 0;
        for (var line: map) {
            System.out.print(String.format("%3d : ", row));
            var col = 0;
            for (var conn: line)
                if (pos.row ==row && pos.col == col++)
                    System.out.print("*");
                else
                    System.out.print(conn.presentation);
            System.out.println();
            row++;
        }
        System.out.println();
    }

    Point findStart(Connection[][] map) {
        for (int row=0; row<map.length; row++)
            for (int col=0; col < map[row].length; col++) {
                var connection = map[row][col];
                if (connection == Connection.START)
                    return new Point(col, row);
            }
        throw new RuntimeException("Found no starting point");
    }

    private boolean contains(Pair<Point, Point> neighbours, Point point) {
        return
            neighbours.getLeft().equals(point) ||
            neighbours.getRight().equals(point);
    }

    public long solve1(Stream<String> stream) {
        return (solve(convertData(stream)).size() +1) / 2;
    }

    public List<Point> solve(Connection[][] map) {
        displayMap(map);
        var start = findStart(map);
        alterMapStartStartPoint(start, map);
        displayMap(map);
        var visited = new ArrayList<Point>();
        var currPos = start;
        while (true) {
            var nextPosCandidates = currPos.connectedTo(map);
            visited.add(currPos);
            if (!visited.contains(nextPosCandidates.getLeft())) {
                currPos = nextPosCandidates.getLeft();
            } else if (!visited.contains(nextPosCandidates.getRight())) {
                currPos = nextPosCandidates.getRight();
            } else
                break;
        }
        return visited;
    }

    private void alterMapStartStartPoint(Point start, Connection[][] map) {
        if (contains(start.add(N).connectedTo(map), start) && contains(start.add(S).connectedTo(map), start))
            map[start.row][start.col] = Connection.NS;
        else if (contains(start.add(E).connectedTo(map), start) && contains(start.add(W).connectedTo(map), start))
            map[start.row][start.col] = Connection.EW;
        else if (contains(start.add(N).connectedTo(map), start) && contains(start.add(E).connectedTo(map), start))
            map[start.row][start.col] = Connection.NE;
        else if (contains(start.add(N).connectedTo(map), start) && contains(start.add(W).connectedTo(map), start))
            map[start.row][start.col] = Connection.NW;
        else if (contains(start.add(S).connectedTo(map), start) && contains(start.add(W).connectedTo(map), start))
            map[start.row][start.col] = Connection.SW;
        else if (contains(start.add(S).connectedTo(map), start) && contains(start.add(E).connectedTo(map), start))
            map[start.row][start.col] = Connection.SE;
        else
            throw new RuntimeException("Could not connect start pos");
    }

    public long solve2(Stream<String> stream) {
        var map = convertData(stream);
        var loop = solve(map);
        var clockwise = isLoopClockwise(map, loop);
        return 0;
    }

    private boolean isLoopClockwise(Connection[][] map, List<Point> loop) {
        int turnSum = 0;
        for (int i=0; i<loop.size(); i++) {
            var pos = loop.get(i);
            displayMap(map, pos);
            var nextPos = loop.get((i+1) % loop.size());
            var conn = map[pos.row][pos.col];
            var neighbours = conn.neighbours(pos);
            int turn = conn.turn;
            var moveIsClockwise = neighbours.getRight().equals(nextPos);
            turnSum += turn * (moveIsClockwise ? 1 : -1);
        }
        if (turnSum == -4) return false;
        if (turnSum == 4) return true;
        throw new RuntimeException("Loop does not have a sum of 4 turns.");
    }

    record Point(int col, int row) implements Comparable<Point> {
        Point add (Point p) { return new Point(col + p.col, row + p.row); }
        @Override public int compareTo(Point o) {
            return Objects.compare(this, o,
                    Comparator.comparing(Point::row)
                    .thenComparing(Point::col));
        }
        Pair<Point, Point> connectedTo(Connection[][] map) { return map[row][col].neighbours(this); }
    }
}
