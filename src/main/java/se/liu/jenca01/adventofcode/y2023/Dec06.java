package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec06 extends Christmas {

    long sampleAnswer1 = 288;
    long sampleAnswer2 = 71503;

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

    private List<Race> convertData1(Stream<String> data) {
        List<String> lines = data.collect(Collectors.toList());
        var times = Arrays.stream(lines.get(0).split(":")[1].strip().split(" +")).map(s -> Long.parseLong(s)).toList();
        var distances = Arrays.stream(lines.get(1).split(":")[1].strip().split(" +")).map(s -> Long.parseLong(s)).toList();
        var races = new ArrayList<Race>();
        for (int i=0; i<times.size(); i++)
            races.add(new Race(times.get(i), distances.get(i)));
        return races;
    }

    private Race convertData2(Stream<String> data) {
        List<String> lines = data.collect(Collectors.toList());
        var time = Long.parseLong(lines.get(0).split(":")[1].replace(" ", ""));
        var distance = Long.parseLong(lines.get(1).split(":")[1].replace(" ", ""));
        return new Race(time, distance);
    }

    public long solve1(Stream<String> stream) {
        var races = convertData1(stream);
        var margin = 1L;
        for(var race: races)
            margin *= calcMargin(race);
        return margin;
    }

    private long calcMargin(Race race) {
        int wins = 0;
        for (int i=1; i<race.length; i++)
            if (calcDist(i, race.length) > race.distance) wins++;
        return wins;
    }

    private long calcDist(long time, long length) {
        return time * (length - time);
    }

    public long solve2(Stream<String> stream) {
        return calcMargin(convertData2(stream));
    }

    record Race(long length, long distance) {}
}
