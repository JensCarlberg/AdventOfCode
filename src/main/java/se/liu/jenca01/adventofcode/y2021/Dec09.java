package se.liu.jenca01.adventofcode.y2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec09 extends Christmas {

    long sampleAnswer1 = 15;
    long sampleAnswer2 = 1134;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec09();
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

    private List<List<Byte>> convertData(Stream<String> data) {
        var lines = toList(data);
        var result = new ArrayList<List<Byte>>();
        for(int i=0; i<lines.size(); i++) {
            var line = lines.get(i);
            var lineResult = new ArrayList<Byte>();
            for(int j=0; j<line.length(); j++) {
                lineResult.add(Byte.parseByte(""+line.charAt(j)));
            }
            result.add(lineResult);
        }
        return result;
    }

    public long solve1(Stream<String> stream) {
        long lowPontRiskLevelSum = 0L;
        var heightMap = convertData(stream);
        var lowPoints = getLowPoints(heightMap);
        for(var lowPoint: lowPoints)
            lowPontRiskLevelSum += heightMap.get(lowPoint.getLeft()).get(lowPoint.getRight()) + 1;
        return lowPontRiskLevelSum;
    }

    private ArrayList<Pair<Integer, Integer>> getLowPoints(List<List<Byte>> heightMap) {
        var lowPoints = new ArrayList<Pair<Integer, Integer>>();
        for(int i=0; i<heightMap.size(); i++)
            for(int j=0; j<heightMap.get(i).size(); j++)
                if (isLocalMin(i, j, heightMap))
                    lowPoints.add(new ImmutablePair<Integer, Integer>(i, j));
        return lowPoints;
    }

    private boolean isLocalMin(int i, int j, List<List<Byte>> heightMap) {
        var height = heightMap.get(i).get(j);
        return
                check(i-1, j, height, heightMap) &&
                check(i+1, j, height, heightMap) &&
                check(i, j-1, height, heightMap) &&
                check(i, j+1, height, heightMap);
    }

    private boolean check(int i, int j, Byte height, List<List<Byte>> heightMap) {
        if (j<0 || i<0 || i>=heightMap.size()) return true;
        var row = heightMap.get(i);
        if (j>=row.size()) return true;
        return height < row.get(j);
    }

    public long solve2(Stream<String> stream) {
        var heightMap = convertData(stream);
        var basinSizes = new ArrayList<Integer>();
        var lowPoints = getLowPoints(heightMap);
        for(var lowPoint: lowPoints)
            basinSizes.add(calcBasinSize(lowPoint.getLeft(), lowPoint.getRight(), heightMap));
        var sortedSizes = basinSizes.stream().sorted(this::compareBasinSize).collect(Collectors.toList());
        return 1L * sortedSizes.get(0) * sortedSizes.get(1) * sortedSizes.get(2);
    }

    private Integer calcBasinSize(int i, int j, List<List<Byte>> heightMap) {
        var basinPoints = new ArrayList<Pair<Integer, Integer>>();
        addPoint(i, j, basinPoints, heightMap);
        return basinPoints.size();
    }

    private void addPoint(int i, int j, ArrayList<Pair<Integer, Integer>> basinPoints, List<List<Byte>> heightMap) {
        if (j<0 || i<0 || i >= heightMap.size() || j >= heightMap.get(i).size()) return;
        if (heightMap.get(i).get(j) == 9) return;
        var point = new ImmutablePair<Integer, Integer>(i, j);
        if (basinPoints.contains(point)) return;
        basinPoints.add(point);
        addPoint(i-1, j, basinPoints, heightMap);
        addPoint(i+1, j, basinPoints, heightMap);
        addPoint(i, j-1, basinPoints, heightMap);
        addPoint(i, j+1, basinPoints, heightMap);
    }

    private int compareBasinSize(Integer i1, Integer i2) {
        return i2.compareTo(i1);
    }
}

/*

--- Day 9: Smoke Basin ---
These caves seem to be lava tubes. Parts are even still volcanically active; small hydrothermal vents release smoke into the caves that slowly settles like rain.

If you can model how the smoke flows through the caves, you might be able to avoid it and be that much safer. The submarine generates a heightmap of the floor of the nearby caves for you (your puzzle input).

Smoke flows to the lowest point of the area it's in. For example, consider the following heightmap:

2199943210
3987894921
9856789892
8767896789
9899965678
Each number corresponds to the height of a particular location, where 9 is the highest and 0 is the lowest a location can be.

Your first goal is to find the low points - the locations that are lower than any of its adjacent locations. Most locations have four adjacent locations (up, down, left, and right); locations on the edge or corner of the map have three or two adjacent locations, respectively. (Diagonal locations do not count as adjacent.)

In the above example, there are four low points, all highlighted: two are in the first row (a 1 and a 0), one is in the third row (a 5), and one is in the bottom row (also a 5). All other locations on the heightmap have some lower adjacent location, and so are not low points.

The risk level of a low point is 1 plus its height. In the above example, the risk levels of the low points are 2, 1, 6, and 6. The sum of the risk levels of all low points in the heightmap is therefore 15.

Find all of the low points on your heightmap. What is the sum of the risk levels of all low points on your heightmap?


--- Part Two ---
Next, you need to find the largest basins so you know what areas are most important to avoid.

A basin is all locations that eventually flow downward to a single low point. Therefore, every low point has a basin, although some basins are very small. Locations of height 9 do not count as being in any basin, and all other locations will always be part of exactly one basin.

The size of a basin is the number of locations within the basin, including the low point. The example above has four basins.

The top-left basin, size 3:

2199943210
3987894921
9856789892
8767896789
9899965678
The top-right basin, size 9:

2199943210
3987894921
9856789892
8767896789
9899965678
The middle basin, size 14:

2199943210
3987894921
9856789892
8767896789
9899965678
The bottom-right basin, size 9:

2199943210
3987894921
9856789892
8767896789
9899965678
Find the three largest basins and multiply their sizes together. In the above example, this is 9 * 14 * 9 = 1134.

What do you get if you multiply together the sizes of the three largest basins?



*/