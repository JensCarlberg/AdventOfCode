package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec14 extends Christmas {

    long sampleAnswer1 = 24;
    long sampleAnswer2 = 93;

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

        var minY = lines.stream().map(l -> getMinY(l)).mapToInt(i -> i).min().getAsInt();
        var maxY = lines.stream().map(l -> getMaxY(l)).mapToInt(i -> i).max().getAsInt() + 1 + 2;
        var minX = lines.stream().map(l -> getMinX(l)).mapToInt(i -> i).min().getAsInt() - minY - 10 - 130;
        var maxX = lines.stream().map(l -> getMaxX(l)).mapToInt(i -> i).max().getAsInt() + 1 + maxY + 10;

        var map = new char[maxY][maxX - minX];
        for (var line: lines)
            addRocks(line, map, minX, minY);
        addRocksAtBottom(map, maxY-1, minX, maxX);

        displayMap(map);

        return new Puzzle(map, minX, minY);
    }

	private void displayMap(char[][] map) {
    	int i=0;
        for (var line: map)
            System.out.println(String.format("%3d: %s", i++, new String(line)));
        System.out.println();
    }

    private void addRocks(String line, char[][] map, int minX, int minY) {
        var points = new ArrayList<Point>();
        points.addAll(Stream.of(line.split(" -> ")).map(p -> Point.point(p)).toList());
        var startPos = points.remove(0);
        for (var point: points) {
            drawLine(startPos, point, map, minX);
            startPos = point;
        }
    }

    private void addRocksAtBottom(char[][] map, int maxY, int minX, int maxX) {
    	drawLine(new Point(minX, maxY), new Point(maxX - 1, maxY), map, minX);
	}

    private void drawLine(Point from, Point to, char[][] map, int minX) {
        if (from.x - to.x == 0)
            drawYLine(from.x - minX, from.y, to.y, map);
        else
            drawXLine(from.x, to.x, from.y, map, minX);
    }

    private void drawXLine(int startX, int endX, int y, char[][] map, int minX) {
        var dir = (startX < endX) ? 1 : -1;
        var pos = startX;
        map[y][pos-minX] = '#';
        do {
            pos += dir;
            map[y][pos-minX] = '#';
        } while (pos != endX);
    }

    private void drawYLine(int x, int startY, int endY, char[][] map) {
        var dir = (startY < endY) ? 1 : -1;
        var pos = startY;
        map[pos][x] = '#';
        do {
            pos += dir;
            map[pos][x] = '#';
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
        var puzzle = convertData(stream);
        var grainCount = 0;
        outer: while (true) {
        	var grainPos = new Point(500, 0);
        	var nextGrainPos = drop(grainPos, puzzle);
        	while (grainPos.compareTo(nextGrainPos) != 0) {
            	grainPos = nextGrainPos;
            	nextGrainPos = drop(grainPos, puzzle);
            	if (nextGrainPos.y >= puzzle.map.length - 2)
            		break outer;
        	}
        	grainCount++;
        	settle(grainPos, puzzle);
        	displayMap(puzzle.map);
        }
        return grainCount;
    }

    private void settle(Point pos, Puzzle puzzle) {
		puzzle.map[pos.y][pos.x - puzzle.offsetX] = 'o';
	}

	private Point drop(Point pos, Puzzle puzzle) {
    	var map = puzzle.map;
    	Point down = new Point(pos.x, pos.y + 1);
    	if (canDrop(map, down, puzzle))
    		return down;
    	Point left = new Point(pos.x - 1, pos.y + 1);
    	if (canDrop(map, left, puzzle))
    		return left;
    	Point right = new Point(pos.x + 1, pos.y + 1);
    	if (canDrop(map, right, puzzle))
    		return right;
		return pos;
	}

	private boolean canDrop(char[][] map, Point pos, Puzzle puzzle) {
		return !inMap(pos, map, puzzle) || isFree(pos, map, puzzle);
	}

	private boolean isFree(Point pos, char[][] map, Puzzle puzzle) {
		var c = map[pos.y][pos.x - puzzle.offsetX];
		return c != '#' && c != 'o';
	}

	private boolean inMap(Point pos, char[][] map, Puzzle puzzle) {
		if (pos.y >= map.length)
			throw new RuntimeException("Sand should not fall this far!");
		return (pos.x - puzzle.offsetX >= 0 &&
				pos.x - puzzle.offsetX < map[0].length &&
				pos.y >= 0 &&
				pos.y < map.length);
	}

	public long solve2(Stream<String> stream) {
        var puzzle = convertData(stream);
        var grainCount = 0;
        outer: while (true) {
        	grainCount++;
        	var grainPos = new Point(500, 0);
        	var nextGrainPos = drop(grainPos, puzzle);
        	if (grainPos.compareTo(nextGrainPos) == 0)
        		break outer;
        	while (grainPos.compareTo(nextGrainPos) != 0) {
            	grainPos = nextGrainPos;
            	nextGrainPos = drop(grainPos, puzzle);
        	}
        	settle(grainPos, puzzle);
        	if (grainCount % 10 == 0) {
        		displayMap(puzzle.map);
        		System.out.print(".");
        	}
        }
        System.out.println();
        displayMap(puzzle.map);
        return grainCount;
    }

    record Puzzle(char[][] map, int offsetX, int offsetY) {}
    record Point(int x, int y) implements Comparable<Point> {
    	@Override
    	public int compareTo(Point o) {
    		return Objects.compare(this, o, Comparator.comparing(Point::x).thenComparing(Point::y));
    	}
        static Point point(String pair) {
            var parts = pair.split(",");
            return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
    }
}

/*



*/