package se.liu.jenca01.adventofcode.y2024;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;
import se.liu.jenca01.adventofcode.utils.data.Point;

public class Dec06 extends Christmas {

    long sampleAnswer1 = 41;
    long sampleAnswer2 = 6;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec06();
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

    private LabMap convertData(Stream<String> data) {
    	var lines = data.toList();
    	var rows = lines.size();
    	var cols = lines.get(0).length();
    	var obstacles = new TreeSet<Point>();
    	Point startPoint = null;
    	for(int row=0; row<rows; row++)
    		for (int col=0; col<cols; col++) {
    			if (lines.get(row).charAt(col) == '#')
    				obstacles.add(new Point(row, col));
    			if (lines.get(row).charAt(col) == '^')
    				startPoint = new Point(row, col);
    		}
        return new LabMap(startPoint, obstacles, rows, cols);
    }

    public long solve1(Stream<String> stream) {
    	var lab = convertData(stream);
    	var dirs = new Point[] {
    			new Point(-1, 0),
    			new Point(0, 1),
    			new Point(1, 0),
    			new Point(0, -1)
    	};
    	var currentDir = 0;
    	var visitedPoints = new TreeSet<Point>();
    	var currentPoint = lab.guardStartPos();
    	while (inLab(currentPoint, lab)) {
    		while (obstacleAhead(currentPoint, dirs[currentDir], lab))
				currentDir = (currentDir + 1) % dirs.length;
    		visitedPoints.add(currentPoint);
    		currentPoint = currentPoint.add(dirs[currentDir]);
    	}
        return visitedPoints.size();
    }

    private boolean inLab(Point currentPoint, LabMap lab) {
    	if (currentPoint.x() < 0 || currentPoint.y() < 0)
    		return false;
    	if (currentPoint.x() >= lab.rows() || currentPoint.y() >= lab.cols())
    		return false;
		return true;
	}

	private boolean obstacleAhead(Point currentPoint, Point point, LabMap lab) {
    	var goingTo = currentPoint.add(point);
    	if (lab.obstacles.contains(goingTo))
    		return true;
		return false;
	}

	public long solve2(Stream<String> stream) {
    	var lab = convertData(stream);
    	var dirs = new Point[] {
    			new Point(-1, 0),
    			new Point(0, 1),
    			new Point(1, 0),
    			new Point(0, -1)
    	};
    	var currentDir = 0;
    	var visitedPoints = new TreeSet<Point>();
    	var visitedPointDirs = new TreeMap<Point, Integer>();
    	var blockadePositions = new TreeSet<Point>();
    	var currentPoint = lab.guardStartPos();
    	while (inLab(currentPoint, lab)) {
    		var oldDir = currentDir;
    		while (obstacleAhead(currentPoint, dirs[currentDir], lab))
				currentDir = (currentDir + 1) % dirs.length;
    		// Needs to look for visited all the way to the right, not only where the guard stands.
    		// Stop if obstacle found or map ends: no loop
    		if (visitedPoints.contains(currentPoint)) {
    			var prevDir = visitedPointDirs.get(currentPoint);
    			if (prevDir == (currentDir + 1) % dirs.length)
    				blockadePositions.add(currentPoint.add(dirs[currentDir]));
    		}
    		visitedPoints.add(currentPoint);
    		visitedPointDirs.put(currentPoint, oldDir);
    		currentPoint = currentPoint.add(dirs[currentDir]);
    	}
        return blockadePositions.size();
    }

	record LabMap (Point guardStartPos, Set<Point> obstacles, int rows, int cols) {}
}
