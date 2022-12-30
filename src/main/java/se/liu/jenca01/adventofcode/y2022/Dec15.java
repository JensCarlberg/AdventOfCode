package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec15 extends Christmas {

    long sampleAnswer1 = 26;
    long sampleAnswer2 = 56000011;
    int sampleRow = 10;
    int puzzleRow = 2000000;
    int sampleSize = 20;
    int puzzleSize = 4000000;
    boolean run1 = true;
    boolean run2 = true;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec15();
        christmas.solveSample();
        christmas.solveMy();
    }

    @Override
    public void solveSample() throws Exception {
    	if (run1) {
    		long start = System.currentTimeMillis();
    		long solve1 = solve1(sampleData(), sampleRow);
    		long stop = System.currentTimeMillis();
    		System.out.println("Example solve1 timing: " + (stop-start) + "ms");
    		assertEquals(sampleAnswer1, solve1, "solve1 is expected to return " + sampleAnswer1);
    	}

    	if (run2) {
    		long start = System.currentTimeMillis();
    		long solve2 = solve2(sampleData(), sampleSize);
    		long stop = System.currentTimeMillis();
    		System.out.println("Example solve2 timing: " + (stop-start) + "ms");
    		assertEquals(sampleAnswer2, solve2, "solve2 is expected to return " + sampleAnswer2);
    	}
    }

    @Override
    public void solveMy() throws Exception {
    	if (run1) System.out.println(simpleClassName() + " solve1: " + solve1(myData(), puzzleRow));
        if (run2) System.out.println(simpleClassName() + " solve2: " + solve2(myData(), puzzleSize));
    }

    private Puzzle convertData(Stream<String> data) {
        var lines = data.toList();
        var sensors = new ArrayList<Sensor>();
        for (var line:lines)
            sensors.add(buildSensor(line));
        return new Puzzle(sensors);
    }

    private Sensor buildSensor(String line) {
        var parts = line.split(": ");
        var sensorPos = Point.from(parts[0].substring(10));
        var beaconPos = Point.from(parts[1].substring(21));
        return new Sensor(sensorPos, beaconPos, sensorPos.distance(beaconPos));
    }

    public long solve1(Stream<String> stream, int row) {
        var puzzle = convertData(stream);
        var controlledSpace = new TreeSet<Point>();
        for (var sensor: puzzle.sensors) {
            var distanceCovered = sensor.pos.distance(sensor.nearestBeacon);
            var x = sensor.pos.x;
            var minX = x - distanceCovered;
            var maxX = x + distanceCovered + 1;
            for (int p=minX; p<maxX; p++) {
                var point = new Point(p, row);
                if (point.distance(sensor.pos) <= distanceCovered)
                    controlledSpace.add(point);
            }
        }
        for (var sensor: puzzle.sensors)
            controlledSpace.remove(sensor.nearestBeacon);
        return controlledSpace.size();
    }

    public long solve2(Stream<String> stream, int max) {
    	return solve2(convertData(stream), max);
    }

    public long solve2(Puzzle puzzle, int max) {
    	for (int x=0; x<=max; x++) {
    		var ranges = new TreeSet<Range>();
    		for (var s: puzzle.sensors) {
    			var y = s.pos.y;
    			Point p = new Point(x, s.pos.y);
    			var distance = p.distance(s.pos);
    			var extra = s.beaconDist - distance;
    			if (extra < 0) continue;
    			ranges.add(new Range(y-extra, y+extra));
    		}
    		var rangesList = new ArrayList<>(ranges);
    		var overlap = rangesList.get(0);
    		for (int i=1; i<rangesList.size(); i++)
    			try {
    				overlap = overlap.consolidate(rangesList.get(i));
    			} catch (Exception e) {
    				return tuning(x, overlap.to + 1);
    			}
    		if (overlap.from > 0)
    			return tuning(x, 0);
    		if (overlap.to < max)
    			return tuning(x, max);
    	}
        throw new RuntimeException();
    }

    private long tuning(int x, int y) {
		return x * 4000000L + y;
	}

    record Point(int x, int y) implements Comparable<Point> {
        int distance(Point o) { return distance(o.x, o.y); }
        int distance(int x, int y) { return Math.abs(this.x - x) + Math.abs(this.y - y); }

        static Point from (String xy) {
            var parts = xy.split(",");
            var x = Integer.parseInt(parts[0].split("=")[1]);
            var y = Integer.parseInt(parts[1].split("=")[1]);
            return new Point(x, y);
        }

        @Override public int compareTo(Point o) { return Objects.compare(this, o, Comparator.comparing(Point::x).thenComparing(Point::y)); }
    }
    record Range(int from, int to) implements Comparable<Range> {
    	Range { assert from <= to; }
    	boolean overlap(Range o) { return !(o.from > to + 1 || o.to + 1 < from); }
    	Range consolidate(Range o) {
    		if (!overlap(o)) throw new IllegalArgumentException();
    		return new Range(Math.min(from, o.from), Math.max(to,  o.to));
    	}
		@Override public int compareTo(Range o) { return Objects.compare(this, o, Comparator.comparing(Range::from).thenComparing(Range::to)); }
    }
    record Sensor(Point pos, Point nearestBeacon, int beaconDist) {
    	boolean isCovered(int x, int y) {
    		return pos.distance(x, y) <= beaconDist;
    	}
    }
    record Puzzle(List<Sensor> sensors) {}
}

/*



*/