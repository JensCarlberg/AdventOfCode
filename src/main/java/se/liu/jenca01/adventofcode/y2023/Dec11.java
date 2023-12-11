package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec11 extends Christmas {

    long sampleAnswer1 = 374;
    long sampleAnswer21  = 1030;
    long sampleAnswer22  = 8410;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec11();
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
        long solve21 = solve2(sampleData(), 10 - 1);
        stop = System.currentTimeMillis();
        System.out.println("Example solve2 2 timing: " + (stop-start) + "ms");
        assertEquals(sampleAnswer21, solve21, "solve2 1 is expected to return " + sampleAnswer21);

        start = System.currentTimeMillis();
        long solve22 = solve2(sampleData(), 100 - 1);
        stop = System.currentTimeMillis();
        System.out.println("Example solve2 1 timing: " + (stop-start) + "ms");
        assertEquals(sampleAnswer22, solve22, "solve2 2 is expected to return " + sampleAnswer22);
    }

    @Override
    public void solveMy() throws Exception {
        System.out.println(simpleClassName() + " solve1: " + solve1(myData()));
        System.out.println(simpleClassName() + " solve2: " + solve2(myData(), 1000000 - 1));
    }

    private Universe convertData(Stream<String> data) {
        var galaxies = new ArrayList<Pos>();
        var lines = data.toList();
        for (int row = 0; row<lines.size(); row++)
            for (int col = 0; col<lines.get(row).length(); col++)
                if (lines.get(row).charAt(col) == '#')
                    galaxies.add(new Pos(row, col));
        return new Universe(galaxies, lines.size(), lines.get(0).length());
    }

    public long solve1(Stream<String> stream) {
        var universe = convertData(stream);
        var emptyRows = findEmptyRows(universe);
        var emptyCols = findEmptyCols(universe);
        var galaxies = universe.galaxies;
        long distSum = 0;
        for (int i=0; i<galaxies.size()-1; i++)
            for(int j=i+1; j<galaxies.size(); j++)
                distSum += distanceBetween(galaxies.get(i), galaxies.get(j), emptyRows, emptyCols, 1);
        return distSum;
    }

    private List<Integer> findEmptyRows(Universe universe) {
        var empty = new ArrayList<Integer>();
        var usedRows = universe.galaxies.stream().map(g -> g.row).distinct().toList();
        for (int i=0; i<universe.maxRow; i++)
            if (!usedRows.contains(i))
                empty.add(i);
        return empty;
    }

    private List<Integer> findEmptyCols(Universe universe) {
        var empty = new ArrayList<Integer>();
        var usedCols = universe.galaxies.stream().map(g -> g.col).distinct().toList();
        for (int i=0; i<universe.maxCol; i++)
            if (!usedCols.contains(i))
                empty.add(i);
        return empty;
    }

    private long distanceBetween(Pos pos, Pos pos2, List<Integer> emptyRows, List<Integer> emptyCols, long ageFactor) {
        var minRow = Math.min(pos.row, pos2.row);
        var maxRow = Math.max(pos.row, pos2.row);
        var minCol = Math.min(pos.col, pos2.col);
        var maxCol = Math.max(pos.col, pos2.col);
        return maxRow - minRow
                + maxCol - minCol
                + countEmpty(minRow, maxRow, emptyRows, ageFactor)
                + countEmpty(minCol, maxCol, emptyCols, ageFactor)
                ;
    }

    private long countEmpty(int min, int max, List<Integer> empties, long ageFactor) {
        long count = 0;
        for (int i=min; i<max; i++)
            if (empties.contains(i))
                count += ageFactor;
        return count;
    }

    public long solve2(Stream<String> stream, long ageFactor) {
        var universe = convertData(stream);
        var emptyRows = findEmptyRows(universe);
        var emptyCols = findEmptyCols(universe);
        var galaxies = universe.galaxies;
        long distSum = 0;
        for (int i=0; i<galaxies.size()-1; i++)
            for(int j=i+1; j<galaxies.size(); j++)
                distSum += distanceBetween(galaxies.get(i), galaxies.get(j), emptyRows, emptyCols, ageFactor);
        return distSum;
    }

    record Pos(int row, int col) {}
    record Universe(List<Pos> galaxies, int maxRow, int maxCol) {}
}
