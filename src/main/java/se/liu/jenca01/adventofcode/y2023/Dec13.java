package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec13 extends Christmas {

    long sampleAnswer1 = 405;
    long sampleAnswer2 = 0;

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

    private List<char[][]> convertData(Stream<String> data) {
        var lines = data.toList();
        var patterns = new ArrayList<char[][]>();
        var patternLines = new ArrayList<String>();
        for (var line: lines) {
            if (line.strip().length() == 0) {
                patterns.add(getPattern(patternLines));
                patternLines.clear();
            } else {
                patternLines.add(line);
            }
        }
        if (patternLines.size() > 0)
            patterns.add(getPattern(patternLines));
        return patterns;
    }

    private void displayPattern(char[][] pattern, int mirrorAt) {
        for (var row=0; row<pattern.length; row++) {
            if (row == mirrorAt) {
                for (var col=0; col<pattern[row].length; col++) System.out.print('-');
                System.out.println();
            }
            for (var col=0; col<pattern[row].length; col++)
                System.out.print(pattern[row][col]);
            System.out.println();
        }
        System.out.println();
    }

    private char[][] getPattern(ArrayList<String> patternLines) {
        var pattern = new char[patternLines.size()][patternLines.get(0).length()];
        for (var row=0; row<patternLines.size(); row++)
            for (var col=0; col<patternLines.get(row).length(); col++)
                pattern[row][col] = patternLines.get(row).charAt(col);
        return pattern;
    }

    public long solve1(Stream<String> stream) {
        var data = convertData(stream);
        long reflectionCalc = 0;
        for (var pattern: data) {
            var reflectVertical = calcReflection(transcribe(pattern));
            if (reflectVertical != 0) {
                reflectionCalc += reflectVertical + 1;
                continue;
            }
            var reflectHorisontal = calcReflection(pattern);
            if (reflectHorisontal != 0) {
                reflectionCalc += (reflectHorisontal + 1) * 100;
            }
        }
        return reflectionCalc;
    }

    private long calcReflection(char[][] pattern) {
        var middle = pattern.length / 2;
        var hasEvenRows = pattern.length % 2 == 0;
        if (hasEvenRows)
            return calcReflection(pattern, middle-1, middle, middle);
        else
            return calcReflection(pattern, middle-1, middle, middle)
                    + calcReflection(pattern, middle, middle+1, middle);
    }

    private long calcReflection(char[][] pattern, int upper, int lower, int noOfRows) {
        displayPattern(pattern, lower);
        for (int row=0; row<noOfRows; row++)
            if (!equals(pattern[upper-row], pattern[lower+row]))
                return 0;
        return noOfRows;
    }

    private boolean equals(char[] upper, char[] lower) {
        for (var col=0; col<upper.length; col++)
            if (upper[col] != lower[col])
                return false;
        return true;
    }

    private char[][] transcribe(char[][] pattern) {
        var transcribed = new char[pattern[0].length][pattern.length];
        for (var row=0; row<pattern.length; row++)
            for (var col=0; col<pattern[row].length; col++)
                transcribed[col][row] = pattern[row][col];
        return transcribed;
    }

    public long solve2(Stream<String> stream) {
        return 0;
    }
}
