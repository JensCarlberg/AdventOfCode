package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec18 extends Christmas {

    long sampleAnswer1 = 64;
    long sampleAnswer2 = 58;

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

    private Set<Point> convertData(Stream<String> data) {
    	var points = new TreeSet<Point>();
        return data.map(s -> Point.from(s)).collect(Collectors.toSet());
    }

    public long solve1(Stream<String> stream) {
    	var puzzle = convertData(stream);
    	int areas = 0;
    	for (var point: puzzle)
    		areas += unconnectedSideCount(point, puzzle);
        return areas;
    }

    private int unconnectedSideCount(Point point, Set<Point> puzzle) {
    	var sides = new TreeSet<Point>();
    	sides.add(new Point(point.x-1, point.y, point.z));
    	sides.add(new Point(point.x+1, point.y, point.z));
    	sides.add(new Point(point.x, point.y-1, point.z));
    	sides.add(new Point(point.x, point.y+1, point.z));
    	sides.add(new Point(point.x, point.y, point.z-1));
    	sides.add(new Point(point.x, point.y, point.z+1));
    	sides.removeAll(puzzle);
		return sides.size();
	}

	public long solve2(Stream<String> stream) {
        return 0;
    }

    record Point(int x, int y, int z) implements Comparable<Point> {
		@Override
		public int compareTo(Point o) {
			return Objects.compare(this, o, Comparator.comparing(Point::x).thenComparing(Point::y).thenComparing(Point::z));
		}
		static Point from(String line) {
			var parts = line.split(",");
			return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
		}
    }
}

/*



*/