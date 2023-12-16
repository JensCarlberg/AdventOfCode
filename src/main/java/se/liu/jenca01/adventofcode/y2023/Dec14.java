package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec14 extends Christmas {

    long sampleAnswer1 = 136;
    long sampleAnswer2 = 64;

    final static char ROLLING_ROCK = 'O';
    final static char FIXED_ROCK = '#';
    final static char EMPTY = '.';

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
        System.out.println(new Date());
        long start = System.currentTimeMillis();
        System.out.println(simpleClassName() + " solve1: " + solve1(myData()));
        long stop = System.currentTimeMillis();
        System.out.println("solve1 timing: " + (stop-start) + "ms");
        System.out.println(new Date());

        System.out.println(simpleClassName() + " solve2: " + solve2(myData()));
    }

    private char[][] convertData(Stream<String> data) {
        var lines = data.toList();
        var map = new char[lines.size()][lines.get(0).length()];
        for (var row=0; row<map.length; row++)
            for (var col=0; col<map[row].length; col++)
                map[row][col] = lines.get(row).charAt(col);
        return map;
    }

    private void displayMap(char[][] map) {
        for (var row=0; row<map.length; row++) {
            System.out.println(map[row]);
        }
    }

    private char[][] rotateMapClockwise(char[][] oldMap) {
        var rowCount = oldMap[0].length;
        var map = new char[oldMap[0].length][oldMap.length];
        for (var row=0; row<map.length; row++)
            for (var col=0; col<map[row].length; col++)
                map[row][col] = oldMap[rowCount - col - 1][row];
        return map;
    }

    private char[][] copyMap(char[][] oldMap) {
        var map = new char[oldMap.length][oldMap[0].length];
        for (var row=0; row<map.length; row++)
            for (var col=0; col<map[row].length; col++)
                map[row][col] = oldMap[row][col];
        return map;
    }

    public long solve1(Stream<String> stream) {
        var map = convertData(stream);
        tiltNorth(map);
        long weight = 0;
        for (var row=0; row<map.length; row++)
            for (var col=0; col<map[row].length; col++)
                if (map[row][col] == ROLLING_ROCK)
                    weight += map.length - row;
        return weight;
    }

    private char[][] tiltNorth(char[][] map) {
        for (var fromRow=1; fromRow<map.length; fromRow++) {
            var filledCols = new ArrayList<Integer>();
            for (var toRow=fromRow; toRow>0; toRow--) {
                for (var col=0; col<map[toRow].length; col++) {
                    if (filledCols.contains(col))
                        continue;
                    if (map[toRow-1][col] != EMPTY)
                        filledCols.add(col);
                    else if (map[toRow][col] == ROLLING_ROCK)
                        swap(map, toRow, col, toRow-1, col);
                }
//                displayMap(map);
            }
        }
        return map;
    }

    private void swap(char[][] map, int fromRow, int fromCol, int toRow, int toCol) {
        var swap = map[fromRow][fromCol];
        map[fromRow][fromCol] = map[toRow][toCol];
        map[toRow][toCol] = swap;
    }

    public long solve2(Stream<String> stream) {
        var map = convertData(stream);
        map = cycle(map);
        long weight = 0;
        for (var row=0; row<map.length; row++)
            for (var col=0; col<map[row].length; col++)
                if (map[row][col] == ROLLING_ROCK)
                    weight += map.length - row;
        return weight;
    }

    private char[][] cycle(char[][] map) {
        for (int i=0; i<1000000000; i++) {
            var mapBefore = copyMap(map);
            tiltNorth(map);
            map = rotateMapClockwise(map);
            tiltNorth(map);
            map = rotateMapClockwise(map);
            tiltNorth(map);
            map = rotateMapClockwise(map);
            tiltNorth(map);
            map = rotateMapClockwise(map);
            if (equals(map, mapBefore))
                break;
            if ((i + 1) % 100000 == 0) System.out.print('.');
            if ((i + 1) % 10000000 == 0) System.out.println();
        }
        return map;
    }

    private boolean equals(char[][] map1, char[][] map2) {
        if (map1.length != map2.length) return false;
        if (map1[0].length != map2[0].length) return false;
        for (var row=0; row<map1.length; row++)
            for (var col=0; col<map1[row].length; col++)
                if (map1[row][col] != map2[row][col])
                    return false;
        return true;
    }
}
