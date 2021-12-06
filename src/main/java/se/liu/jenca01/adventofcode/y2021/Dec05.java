package se.liu.jenca01.adventofcode.y2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec05 extends Christmas {

    long answer1 = 5;
    long answer2 = 12;

    Pattern p = Pattern.compile("(\\d+),(\\d+) -> (\\d+),(\\d+)");

    public static void main(String[] args) throws Exception {
        var christmas = new Dec05();
        christmas.solveSample();
        christmas.solveMy();
    }

    @Override
    public void solveSample() throws Exception {
        assertEquals(answer1, solve1(sampleData()), "solve1 is expected to return " + answer1);
        assertEquals(answer2, solve2(sampleData()), "solve2 is expected to return " + answer2);
    }

    @Override
    public void solveMy() throws Exception {
        System.out.println(simpleClassName() + " solve1: " + solve1(myData()));
        System.out.println(simpleClassName() + " solve2: " + solve2(myData()));
    }

    private List<Pair<Pair<Long,Long>,Pair<Long,Long>>> convertSolve1Data(Stream<String> data) throws Exception {
        return data
                .map(s -> parseLine(s))
                .filter(p -> filter(p))
                .collect(Collectors.toList());
    }

    private List<Pair<Pair<Long,Long>,Pair<Long,Long>>> convertSolve2Data(Stream<String> data) throws Exception {
        return data
                .map(s -> parseLine(s))
                .collect(Collectors.toList());
    }

    private boolean filter(Pair<Pair<Long,Long>, Pair<Long,Long>> p) {
        return p.getLeft().getLeft() .equals(p.getRight().getLeft()) ||
                p.getLeft().getRight().equals(p.getRight().getRight());
    }

    private Pair<Pair<Long,Long>, Pair<Long,Long>> parseLine(String s) throws RuntimeException{
        var m = p.matcher(s);
        if (!m.matches()) throw new RuntimeException("No match");
        return new ImmutablePair<Pair<Long, Long>, Pair<Long,Long>>(
                new ImmutablePair<Long, Long>(Long.parseLong(m.group(1)), Long.parseLong(m.group(2))), 
                new ImmutablePair<Long, Long>(Long.parseLong(m.group(3)), Long.parseLong(m.group(4)))
                );
    }

    public long solve1(Stream<String> stream) throws Exception {
        var data = convertSolve1Data(stream);
        var seaFloor = new HashMap<Pair<Long, Long>, Long>(); 
        for(var line: data)
            addLine(seaFloor, line);
        return countOverlaps(seaFloor);
    }

    private long countOverlaps(HashMap<Pair<Long, Long>, Long> seaFloor) {
        return seaFloor.values().stream().filter(v -> v > 1).count();
    }

    private void addLine(HashMap<Pair<Long, Long>, Long> seaFloor, Pair<Pair<Long, Long>, Pair<Long, Long>> line) {
        if (line.getLeft().getLeft().equals(line.getRight().getLeft()))
            addLine(seaFloor, line, getLineDir(line.getLeft().getLeft(), line.getRight().getLeft()), getLineDir(line.getLeft().getRight(), line.getRight().getRight()));
        else
            addLine(seaFloor, line, getLineDir(line.getLeft().getLeft(), line.getRight().getLeft()), getLineDir(line.getLeft().getRight(), line.getRight().getRight()));
    }

    private int getLineDir(long start, long end) {
        if (start < end) return 1;
        if (start == end) return 0;
        return -1;
    }

    private void addLine(HashMap<Pair<Long, Long>, Long> seaFloor, Pair<Pair<Long, Long>, Pair<Long, Long>> line, int xInc, int yInc) {
        var start = line.getLeft();
        var end = line.getRight();
        long x = start.getLeft();
        long y = start.getRight();
        while (x != end.getLeft() || y != end.getRight()) {
            addToSeaFloor(seaFloor, x, y);
            x += xInc;
            y += yInc;
        }
        addToSeaFloor(seaFloor, x, y);
    }

    private void addToSeaFloor(HashMap<Pair<Long, Long>, Long> seaFloor, long x, long y) {
        var pos = new ImmutablePair<>(x, y);
        var count = seaFloor.get(pos);
        if (count == null)
            seaFloor.put(pos,  1L);
        else
            seaFloor.put(pos,  count + 1);
    }

    public long solve2(Stream<String> stream) throws Exception {
        var data = convertSolve2Data(stream);
        var seaFloor = new HashMap<Pair<Long, Long>, Long>(); 
        for(var line: data)
            addLine(seaFloor, line);
        return countOverlaps(seaFloor);
    }
}

/*

--- Day 5: Hydrothermal Venture ---
You come across a field of hydrothermal vents on the ocean floor! These vents constantly produce large, opaque clouds, so it would be best to avoid them if possible.

They tend to form in lines; the submarine helpfully produces a list of nearby lines of vents (your puzzle input) for you to review. For example:

0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2
Each line of vents is given as a line segment in the format x1,y1 -> x2,y2 where x1,y1 are the coordinates of one end the line segment and x2,y2 are the coordinates of the other end. These line segments include the points at both ends. In other words:

An entry like 1,1 -> 1,3 covers points 1,1, 1,2, and 1,3.
An entry like 9,7 -> 7,7 covers points 9,7, 8,7, and 7,7.
For now, only consider horizontal and vertical lines: lines where either x1 = x2 or y1 = y2.

So, the horizontal and vertical lines from the above list would produce the following diagram:

.......1..
..1....1..
..1....1..
.......1..
.112111211
..........
..........
..........
..........
222111....
In this diagram, the top left corner is 0,0 and the bottom right corner is 9,9. Each position is shown as the number of lines which cover that point or . if no line covers that point. The top-left pair of 1s, for example, comes from 2,2 -> 2,1; the very bottom row is formed by the overlapping lines 0,9 -> 5,9 and 0,9 -> 2,9.

To avoid the most dangerous areas, you need to determine the number of points where at least two lines overlap. In the above example, this is anywhere in the diagram with a 2 or larger - a total of 5 points.

Consider only horizontal and vertical lines. At how many points do at least two lines overlap?


--- Part Two ---
Unfortunately, considering only horizontal and vertical lines doesn't give you the full picture; you need to also consider diagonal lines.

Because of the limits of the hydrothermal vent mapping system, the lines in your list will only ever be horizontal, vertical, or a diagonal line at exactly 45 degrees. In other words:

An entry like 1,1 -> 3,3 covers points 1,1, 2,2, and 3,3.
An entry like 9,7 -> 7,9 covers points 9,7, 8,8, and 7,9.
Considering all lines from the above example would now produce the following diagram:

1.1....11.
.111...2..
..2.1.111.
...1.2.2..
.112313211
...1.2....
..1...1...
.1.....1..
1.......1.
222111....
You still need to determine the number of points where at least two lines overlap. In the above example, this is still anywhere in the diagram with a 2 or larger - now a total of 12 points.

Consider all of the lines. At how many points do at least two lines overlap?



*/