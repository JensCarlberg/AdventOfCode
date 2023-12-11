package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec09 extends Christmas {

    long sampleAnswer1 = 114;
    long sampleAnswer2 = 2;

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

    private List<History> convertData(Stream<String> data) {
        var lines = data.toList();
        var histories = new ArrayList<History>();
        for (var line: lines)
            histories.add(new History(Arrays.stream(line.strip().split(" +")).map(v -> Long.parseLong(v)).toList()));
        return histories;
    }

    public long solve1(Stream<String> stream) {
        var histories = convertData(stream);
        long histSum = 0;
        for (var history: histories)
            histSum += calcNextValue(history);
        return histSum;
    }

    private long calcNextValue(History history) {
        if (history.isAllZeroes()) return 0;
        return history.last() + calcNextValue(history.diffs());
    }

    public long solve2(Stream<String> stream) {
        var histories = convertData(stream);
        long histSum = 0;
        for (var history: histories)
            histSum += calcPrevValue(history);
        return histSum;
    }

    private long calcPrevValue(History history) {
        if (history.isAllZeroes()) return 0;
        return history.first() - calcPrevValue(history.diffs());
    }

    record History(List<Long> values) {
        boolean isAllZeroes() {
            var distinctValues = values.stream().distinct().toList();
            return distinctValues.size() == 1 && distinctValues.get(0) == 0;
        }
        History diffs() {
            var diffs = new ArrayList<Long>();
            for (int i=1; i<values.size(); i++)
                diffs.add(values.get(i) - values.get(i-1));
            return new History(diffs);
        }
        long last() { return values.get(values.size() - 1); }
        long first() { return values.get(0); }
    }
}
