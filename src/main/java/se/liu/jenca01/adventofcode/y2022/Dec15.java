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

    public static void main(String[] args) throws Exception {
        var christmas = new Dec15();
        christmas.solveSample();
        christmas.solveMy();
    }

    @Override
    public void solveSample() throws Exception {
        long start = System.currentTimeMillis();
        long solve1 = solve1(sampleData(), sampleRow);
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
        System.out.println(simpleClassName() + " solve1: " + solve1(myData(), puzzleRow));
        System.out.println(simpleClassName() + " solve2: " + solve2(myData()));
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
        return new Sensor(sensorPos, beaconPos);
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

    public long solve2(Stream<String> stream) {
        return 0;
    }
    
    record Point(int x, int y) implements Comparable<Point> {
        int distance(Point o) { return Math.abs(x - o.x) + Math.abs(y - o.y); }
        
        static Point from (String xy) {
            var parts = xy.split(",");
            var x = Integer.parseInt(parts[0].split("=")[1]);
            var y = Integer.parseInt(parts[1].split("=")[1]);
            return new Point(x, y);
        }

        @Override public int compareTo(Point o) { return Objects.compare(this, o, Comparator.comparing(Point::x).thenComparing(Point::y)); }
    }
    record Sensor(Point pos, Point nearestBeacon) {}
    record Puzzle(List<Sensor> sensors) {}
}

/*



*/