package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec12 extends Christmas {

    long sampleAnswer1 = 21;
    long sampleAnswer2 = 525152;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec12();
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

    private List<Conditions> convertData1(Stream<String> data) {
        var lines = data.toList();
        var conditionsData = new ArrayList<Conditions>();
        for (var line: lines)
            conditionsData.add(parseLine1(line));
        return conditionsData;
    }

    private List<Conditions> convertData2(Stream<String> data) {
        var lines = data.toList();
        var conditionsData = new ArrayList<Conditions>();
        for (var line: lines)
            conditionsData.add(parseLine2(line));
        return conditionsData;
    }

    private Conditions parseLine1(String line) {
        var parts = line.split(" ");
        var conditions = parts[0];
        var counts = parseCounts(parts[1]);
        return new Conditions(conditions, counts);
    }

    private Conditions parseLine2(String line) {
        var parts = line.split(" ");
        var conditions = parts[0];
        var counts = parseCounts(parts[1]);
        var unfoldedConditions = String.format("%s?%s?%s?%s?%s", conditions, conditions, conditions, conditions, conditions);
        var unfoldedCounts = new ArrayList<Integer>();
        unfoldedCounts.addAll(counts);
        unfoldedCounts.addAll(counts);
        unfoldedCounts.addAll(counts);
        unfoldedCounts.addAll(counts);
        unfoldedCounts.addAll(counts);
        return new Conditions(unfoldedConditions, unfoldedCounts);
    }

    private List<Integer> parseCounts(String counts) {
        var parts = counts.split(",");
        return Arrays.stream(parts).map(s -> Integer.parseInt(s.strip())).toList();
    }

    public long solve1(Stream<String> stream) {
        var data = convertData1(stream);
        return data.stream().mapToLong(c -> calcPermutations(c)).sum();
    }

    private long calcPermutations(Conditions c) {
        return calcPermutations(c.conditions.toCharArray(), 0, c.counts);
    }

    private long calcPermutations(char[] conds, int pos, List<Integer> counts) {
        if (pos == conds.length)
            return checkValid(conds, counts);
        if (!partialValid(conds, pos, counts))
            return 0;
        if (conds[pos] == '?')
            return
                    calcPermutations(copyWithDot(conds, pos), pos + 1, counts) +
                    calcPermutations(copyWithHash(conds, pos), pos + 1, counts);
        return calcPermutations(conds, pos + 1, counts);
    }

    private boolean partialValid(char[] conds, int pos, List<Integer> counts) {
        var parts = new String(conds).substring(0, pos).replace('.', ' ').strip().split(" +");
        if (parts.length > counts.size())
            return false;
        for (int i=0; i<parts.length; i++)
            if (parts[i].length() > counts.get(i))
                return false;
        return true;
    }

    private long checkValid(char[] conds, List<Integer> counts) {
        var parts = new String(conds).replace('.', ' ').strip().split(" +");
        if (parts.length != counts.size())
            return 0;
        for (int i=0; i<parts.length; i++)
            if (parts[i].length() != counts.get(i))
                return 0;
        return 1;
    }

    private char[] copyWithDot(char[] conds, int pos) {
        return copyWith(conds, pos, '.');
    }

    private char[] copyWithHash(char[] conds, int pos) {
        return copyWith(conds, pos, '#');
    }

    private char[] copyWith(char[] conds, int pos, char c) {
        var newConds = conds.clone();
        newConds[pos] = c;
        return newConds;
    }

    public long solve2(Stream<String> stream) {
        var data = convertData2(stream);
        return data.stream().mapToLong(c -> calcPermutations(c)).sum();
    }

    record Conditions(String conditions, List<Integer> counts) {}
}
