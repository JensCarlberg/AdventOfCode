package se.liu.jenca01.adventofcode.y2024;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;
import se.liu.jenca01.adventofcode.utils.data.Point;

public class Dec04 extends Christmas {

    private static final Point UL = new Point(-1, -1);
    private static final Point U  = new Point(-1,  0);
    private static final Point UR = new Point(-1,  1);
    private static final Point L  = new Point( 0, -1);
    private static final Point R  = new Point( 0,  1);
    private static final Point DL = new Point( 1, -1);
    private static final Point D  = new Point( 1,  0);
    private static final Point DR = new Point( 1,  1);

    long sampleAnswer1 = 18;
    long sampleAnswer2 = 9;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec04();
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
    	var map = new char[lines.size()][];
    	var row = 0;
    	for (var line: lines)
			map[row++] = line.toCharArray();
        return map;
    }

    public long solve1(Stream<String> stream) {
    	var map = convertData(stream);
    	var xPos = getAllXPos(map);
    	var count = 0L;
    	for(var pos: xPos)
			count += findXmas(map, pos);
        return count;
    }

    private long findXmas(char[][] map, Point pos) {
    	var dirs = findM(map, pos);
    	var count = 0L;
    	for (var dir: dirs)
    		if (findXmas(map, pos, dir))
    			count++;

		return count;
	}

    private boolean findXmas(char[][] map, Point pos, Point dir) {
    	var mPos = pos.add(dir);
    	var aPos = mPos.add(dir);
    	var sPos = aPos.add(dir);

    	if (charAt(aPos, map) != 'A')
			return false;
    	if (charAt(sPos, map) != 'S')
			return false;

		return true;
	}

	private Set<Point> findM(char[][] map, Point pos) {
		var dirs = new TreeSet<Point>();
	    if (charAt(pos.add(UL), map) == 'M') dirs.add(UL);
	    if (charAt(pos.add(U) , map) == 'M') dirs.add(U);
	    if (charAt(pos.add(UR), map) == 'M') dirs.add(UR);
	    if (charAt(pos.add(L) , map) == 'M') dirs.add(L);
	    if (charAt(pos.add(R) , map) == 'M') dirs.add(R);
	    if (charAt(pos.add(DL), map) == 'M') dirs.add(DL);
	    if (charAt(pos.add(D) , map) == 'M') dirs.add(D);
	    if (charAt(pos.add(DR), map) == 'M') dirs.add(DR);

		return dirs;
	}

	private boolean outOfBounds(Point pos, char[][] map) {
		return
				pos.x() < 0
				|| pos.y() < 0
				|| pos.y() >= map.length
				|| pos.x() >= map[pos.y()].length;
	}

	private char charAt(Point sPos, char[][] map) {
		if (outOfBounds(sPos, map)) return '.';
		return map[sPos.y()][sPos.x()];
	}

	private Set<Point> getAllXPos(char[][] map) {
		return getAllPos(map, 'X');
	}

	private Set<Point> getAllPos(char[][] map, char searchFor) {
    	var xPos = new TreeSet<Point>();
    	for (int y=0; y<map.length; y++)
			for(int x=0; x<map[y].length; x++)
				if (map[y][x] == searchFor)
					xPos.add(new Point(x, y));
    	return xPos;
	}

	public long solve2(Stream<String> stream) {
		var map = convertData(stream);
		var aPos = getAllPos(map, 'A');
		var count = 0L;
		for (var pos: aPos)
			if (isMasCenter(map, pos))
				count++;
        return count;
    }

	private boolean isMasCenter(char[][] map, Point pos) {
		if (charAt(pos.add(UL), map) == 'M' && charAt(pos.add(DR), map) == 'S' && charAt(pos.add(UR), map) == 'M' && charAt(pos.add(DL), map) == 'S') return true;
		if (charAt(pos.add(UL), map) == 'M' && charAt(pos.add(DR), map) == 'S' && charAt(pos.add(DL), map) == 'M' && charAt(pos.add(UR), map) == 'S') return true;
		if (charAt(pos.add(UR), map) == 'M' && charAt(pos.add(DL), map) == 'S' && charAt(pos.add(UL), map) == 'M' && charAt(pos.add(DR), map) == 'S') return true;
		if (charAt(pos.add(UR), map) == 'M' && charAt(pos.add(DL), map) == 'S' && charAt(pos.add(DR), map) == 'M' && charAt(pos.add(UL), map) == 'S') return true;
		if (charAt(pos.add(DL), map) == 'M' && charAt(pos.add(UR), map) == 'S' && charAt(pos.add(UL), map) == 'M' && charAt(pos.add(DR), map) == 'S') return true;
		if (charAt(pos.add(DL), map) == 'M' && charAt(pos.add(UR), map) == 'S' && charAt(pos.add(DR), map) == 'M' && charAt(pos.add(UL), map) == 'S') return true;
		if (charAt(pos.add(DR), map) == 'M' && charAt(pos.add(UL), map) == 'S' && charAt(pos.add(UR), map) == 'M' && charAt(pos.add(DL), map) == 'S') return true;
		if (charAt(pos.add(DR), map) == 'M' && charAt(pos.add(UL), map) == 'S' && charAt(pos.add(DL), map) == 'M' && charAt(pos.add(UR), map) == 'S') return true;

		return false;
	}
}
