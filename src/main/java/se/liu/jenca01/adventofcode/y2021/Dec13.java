package se.liu.jenca01.adventofcode.y2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec13 extends Christmas {

    long sampleAnswer1 = 17;
    long sampleAnswer2 = 0;

    long maxX;
    long maxY;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec13();
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

    private Pair<Set<Pair<Long,Long>>,List<String>> convertData(Stream<String> data) {
        var lines = toList(data);
        var posLines = new ArrayList<String>();
        var foldLines = new ArrayList<String>();
        
        var pos=0;
        while (lines.get(pos).trim().length() > 0)
            posLines.add(lines.get(pos++));

        for (var i=pos+1; i<lines.size(); i++)
            foldLines.add(lines.get(i));

        return new ImmutablePair<Set<Pair<Long, Long>>, List<String>>(buildMap(posLines), foldLines);
    }

    private Set<Pair<Long, Long>> buildMap(ArrayList<String> posLines) {
        var map = new HashSet<Pair<Long, Long>>();
        for(var line: posLines) {
            var parts = line.split(",");
            map.add(new ImmutablePair<Long, Long>(Long.parseLong(parts[0]), Long.parseLong(parts[1])));
        }
        return map;
    }

    public long solve1(Stream<String> stream) {
        var data = convertData(stream);
        var map = data.getLeft();
        var fold = new ArrayList<String>();
        fold.add(data.getRight().get(0));
        return foldAndCount(map, fold);
    }

    private long foldAndCount(Set<Pair<Long, Long>> map, List<String> fold) {
        maxX = map.stream().map(p -> p.getLeft()*-1).sorted().findFirst().get() * -1;
        maxY = map.stream().map(p -> p.getRight()*-1).sorted().findFirst().get() * -1;

        for (var foldLine: fold)
            if (foldLine.contains(" x="))
                maxX = foldX(map, Long.parseLong(foldLine.split("=")[1]), maxX, maxY);
            else
                maxY = foldY(map, Long.parseLong(foldLine.split("=")[1]), maxX, maxY);

        return count(map, maxX, maxY);
    }

    private long count(Set<Pair<Long, Long>> map, long maxX, long maxY) {
        var count = 0L;
        for(long x=0; x<=maxX; x++)
            for(long y=0; y<=maxY; y++)
                if (map.contains(new ImmutablePair<Long, Long>(x, y)))
                    count++;
        return count;
    }

    private long foldX(Set<Pair<Long, Long>> map, long foldX, long maxX, long maxY) {
        for(long x=foldX+1; x<=maxX; x++)
            for(long y=0; y<=maxY; y++)
                if (map.contains(new ImmutablePair<Long, Long>(x, y)))
                    map.add(new ImmutablePair<Long, Long>(maxX-x, y));
        return foldX - 1;
    }

    private long foldY(Set<Pair<Long, Long>> map, long foldY, long maxX, long maxY) {
        for(long x=0; x<=maxX; x++)
            for(long y=foldY+1; y<=maxY; y++)
                if (map.contains(new ImmutablePair<Long, Long>(x, y)))
                    map.add(new ImmutablePair<Long, Long>(x, maxY-y));
        return foldY - 1;
    }

    public long solve2(Stream<String> stream) {
        var data = convertData(stream);
        var map = data.getLeft();
        var fold = data.getRight();
        foldAndCount(map, fold);
        displayMap(map);
        return 0;
    }

    private void displayMap(Set<Pair<Long, Long>> map) {
        for(long y=0; y<=maxY; y++) {
            for(long x=0; x<=maxX; x++)
                if (map.contains(new ImmutablePair<Long, Long>(x, y)))
                    System.out.print("#");
                else
                    System.out.print(" ");
            System.out.println();
        }
    }
}

/*

--- Day 13: Transparent Origami ---
You reach another volcanically active part of the cave. It would be nice if you could do some kind of thermal imaging so you could tell ahead of time which caves are too hot to safely enter.

Fortunately, the submarine seems to be equipped with a thermal camera! When you activate it, you are greeted with:

Congratulations on your purchase! To activate this infrared thermal imaging
camera system, please enter the code found on page 1 of the manual.
Apparently, the Elves have never used this feature. To your surprise, you manage to find the manual; as you go to open it, page 1 falls out. It's a large sheet of transparent paper! The transparent paper is marked with random dots and includes instructions on how to fold it up (your puzzle input). For example:

6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5
The first section is a list of dots on the transparent paper. 0,0 represents the top-left coordinate. The first value, x, increases to the right. The second value, y, increases downward. So, the coordinate 3,0 is to the right of 0,0, and the coordinate 0,7 is below 0,0. The coordinates in this example form the following pattern, where # is a dot on the paper and . is an empty, unmarked position:

...#..#..#.
....#......
...........
#..........
...#....#.#
...........
...........
...........
...........
...........
.#....#.##.
....#......
......#...#
#..........
#.#........
Then, there is a list of fold instructions. Each instruction indicates a line on the transparent paper and wants you to fold the paper up (for horizontal y=... lines) or left (for vertical x=... lines). In this example, the first fold instruction is fold along y=7, which designates the line formed by all of the positions where y is 7 (marked here with -):

...#..#..#.
....#......
...........
#..........
...#....#.#
...........
...........
-----------
...........
...........
.#....#.##.
....#......
......#...#
#..........
#.#........
Because this is a horizontal line, fold the bottom half up. Some of the dots might end up overlapping after the fold is complete, but dots will never appear exactly on a fold line. The result of doing this fold looks like this:

#.##..#..#.
#...#......
......#...#
#...#......
.#.#..#.###
...........
...........
Now, only 17 dots are visible.

Notice, for example, the two dots in the bottom left corner before the transparent paper is folded; after the fold is complete, those dots appear in the top left corner (at 0,0 and 0,1). Because the paper is transparent, the dot just below them in the result (at 0,3) remains visible, as it can be seen through the transparent paper.

Also notice that some dots can end up overlapping; in this case, the dots merge together and become a single dot.

The second fold instruction is fold along x=5, which indicates this line:

#.##.|#..#.
#...#|.....
.....|#...#
#...#|.....
.#.#.|#.###
.....|.....
.....|.....
Because this is a vertical line, fold left:

#####
#...#
#...#
#...#
#####
.....
.....
The instructions made a square!

The transparent paper is pretty big, so for now, focus on just completing the first fold. After the first fold in the example above, 17 dots are visible - dots that end up overlapping after the fold is completed count as a single dot.

How many dots are visible after completing just the first fold instruction on your transparent paper?


--- Part Two ---
Finish folding the transparent paper according to the instructions. The manual says the code is always eight capital letters.

What code do you use to activate the infrared thermal imaging camera system?

*/