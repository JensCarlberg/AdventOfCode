package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec14 extends Christmas {

    long sampleAnswer1 = 24;
    long sampleAnswer2 = 0;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec14();
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
        
        var maxX = lines.stream().map(l -> getMaxX(l)).mapToInt(i -> i).max().getAsInt() + 1;
        var maxY = lines.stream().map(l -> getMaxY(l)).mapToInt(i -> i).max().getAsInt() + 1;
        var minX = lines.stream().map(l -> getMinX(l)).mapToInt(i -> i).min().getAsInt();
        var minY = lines.stream().map(l -> getMinY(l)).mapToInt(i -> i).min().getAsInt();
        
        var map = new char[maxX - minX][maxY - minY];
        for (var line: lines)
            addRocks(line, map, minX, minY);
        
        displayMap(map);
        
        return new Puzzle(map, minX, minY);
    }

    private void displayMap(char[][] map) {
        for (var line: map)
            System.out.println(new String(line));
        System.out.println();
    }

    private void addRocks(String line, char[][] map, int minX, int minY) {
        var points = new ArrayList<Point>();
        points.addAll(Stream.of(line.split(" -> ")).map(p -> Point.point(p)).toList());
        var startPos = points.remove(0);
        for (var point: points) {
            drawLine(startPos, point, map, minX, minY);
            startPos = point;
        }
    }

    private void drawLine(Point from, Point to, char[][] map, int minX, int minY) {
        if (from.x - to.x == 0)
            drawYLine(from.x - minX, from.y, to.y, map, minY);
        else
            drawXLine(from.x, to.x, from.y - minY, map, minX);
    }

    private void drawXLine(int startX, int endX, int y, char[][] map, int minX) {
        var dir = (startX < endX) ? 1 : -1;
        var pos = startX;
        map[pos-minX][y] = '#';
        do {
            pos += dir;
            map[pos-minX][y] = '#';
        } while (pos != endX); 
    }

    private void drawYLine(int x, int startY, int endY, char[][] map, int minY) {
        var dir = (startY < endY) ? 1 : -1;
        var pos = startY;
        map[x][pos-minY] = '#';
        do {
            pos += dir;
            map[x][pos-minY] = '#';
        } while (pos != endY); 
    }

    private int getMaxX(String line) {
        var parts = line.split(" -> ");
        var maxX = Integer.MIN_VALUE;
        for (var part: parts) {
            int x = Integer.parseInt(part.split(",")[0]);
            if (x > maxX) maxX = x;
        }
        return maxX;
    }

    private int getMaxY(String line) {
        var parts = line.split(" -> ");
        var maxY = Integer.MIN_VALUE;
        for (var part: parts) {
            int y = Integer.parseInt(part.split(",")[1]);
            if (y > maxY) maxY = y;
        }
        return maxY;
    }

    private int getMinX(String line) {
        var parts = line.split(" -> ");
        var minX = Integer.MAX_VALUE;
        for (var part: parts) {
            int x = Integer.parseInt(part.split(",")[0]);
            if (x < minX) minX = x;
        }
        return minX;
    }

    private int getMinY(String line) {
        var parts = line.split(" -> ");
        var minY = Integer.MAX_VALUE;
        for (var part: parts) {
            int y = Integer.parseInt(part.split(",")[1]);
            if (y < minY) minY = y;
        }
        return minY;
    }

    public long solve1(Stream<String> stream) {
        var ouzzle = convertData(stream);
        return 0;
    }

    public long solve2(Stream<String> stream) {
        return 0;
    }
    
    record Puzzle(char[][] map, int offsetX, int offsetY) {}
    record Point(int x, int y) {
        static Point point(String pair) {
            var parts = pair.split(",");
            return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
    }
}

/*



*/