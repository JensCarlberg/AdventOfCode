package se.liu.jenca01.adventofcode.y2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec07 extends Christmas {

    long sampleAnswer1 = 37;
    long sampleAnswer2 = 168;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec07();
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

    private List<Long> convertData(Stream<String> data) {
        return data.map(s -> Long.parseLong(s)).collect(Collectors.toList());
    }

    public long solve1(Stream<String> stream) {
        var data = convertData(Stream.of(toList(stream).get(0).split(",")));
        var minPos = data.stream().collect(Collectors.minBy(this::longComp)).get().longValue();
        var maxPos = data.stream().collect(Collectors.maxBy(this::longComp)).get().longValue();
        var fuelConsumed = new HashMap<Long, Long>();
        for (long pos=minPos; pos <= maxPos; pos++)
            fuelConsumed.put(pos, calcFuel(pos, data));
        return fuelConsumed.values().stream().collect(Collectors.minBy(this::longComp)).get().longValue();
    }

    private long calcFuel(long pos, List<Long> data) {
        var fuel = 0L;
        for(var crabPos: data)
            fuel += Math.abs(pos - crabPos);
        return fuel;
    }

    public long solve2(Stream<String> stream) {
        var data = convertData(Stream.of(toList(stream).get(0).split(",")));
        var minPos = data.stream().collect(Collectors.minBy(this::longComp)).get().longValue();
        var maxPos = data.stream().collect(Collectors.maxBy(this::longComp)).get().longValue();
        var fuelConsumed = new HashMap<Long, Long>();
        for (long pos=minPos; pos <= maxPos; pos++)
            fuelConsumed.put(pos, calcNonLinearFuel(pos, data));
        return fuelConsumed.values().stream().collect(Collectors.minBy(this::longComp)).get().longValue();
    }

    private long calcNonLinearFuel(long pos, List<Long> data) {
        var fuel = 0L;
        for(var crabPos: data)
            fuel += calcSeries(Math.abs(pos - crabPos));
        return fuel;
    }

    private long calcSeries(long length) {
        if (length == 0) return 0;
        if (length == 1) return 1;
        return (1+length)*length/2;
    }

    private int longComp(Long t1, Long t2) {
        return t1.compareTo(t2);
    }
}

/*



 */