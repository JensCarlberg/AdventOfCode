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

--- Day 7: The Treachery of Whales ---
A giant whale has decided your submarine is its next meal, and it's much faster than you are. There's nowhere to run!

Suddenly, a swarm of crabs (each in its own tiny submarine - it's too deep for them otherwise) zooms in to rescue you! They seem to be preparing to blast a hole in the ocean floor; sensors indicate a massive underground cave system just beyond where they're aiming!

The crab submarines all need to be aligned before they'll have enough power to blast a large enough hole for your submarine to get through. However, it doesn't look like they'll be aligned before the whale catches you! Maybe you can help?

There's one major catch - crab submarines can only move horizontally.

You quickly make a list of the horizontal position of each crab (your puzzle input). Crab submarines have limited fuel, so you need to find a way to make all of their horizontal positions match while requiring them to spend as little fuel as possible.

For example, consider the following horizontal positions:

16,1,2,0,4,2,7,1,2,14
This means there's a crab with horizontal position 16, a crab with horizontal position 1, and so on.

Each change of 1 step in horizontal position of a single crab costs 1 fuel. You could choose any horizontal position to align them all on, but the one that costs the least fuel is horizontal position 2:

Move from 16 to 2: 14 fuel
Move from 1 to 2: 1 fuel
Move from 2 to 2: 0 fuel
Move from 0 to 2: 2 fuel
Move from 4 to 2: 2 fuel
Move from 2 to 2: 0 fuel
Move from 7 to 2: 5 fuel
Move from 1 to 2: 1 fuel
Move from 2 to 2: 0 fuel
Move from 14 to 2: 12 fuel
This costs a total of 37 fuel. This is the cheapest possible outcome; more expensive outcomes include aligning at position 1 (41 fuel), position 3 (39 fuel), or position 10 (71 fuel).

Determine the horizontal position that the crabs can align to using the least fuel possible. How much fuel must they spend to align to that position?


--- Part Two ---
The crabs don't seem interested in your proposed solution. Perhaps you misunderstand crab engineering?

As it turns out, crab submarine engines don't burn fuel at a constant rate. Instead, each change of 1 step in horizontal position costs 1 more unit of fuel than the last: the first step costs 1, the second step costs 2, the third step costs 3, and so on.

As each crab moves, moving further becomes more expensive. This changes the best horizontal position to align them all on; in the example above, this becomes 5:

Move from 16 to 5: 66 fuel
Move from 1 to 5: 10 fuel
Move from 2 to 5: 6 fuel
Move from 0 to 5: 15 fuel
Move from 4 to 5: 1 fuel
Move from 2 to 5: 6 fuel
Move from 7 to 5: 3 fuel
Move from 1 to 5: 10 fuel
Move from 2 to 5: 6 fuel
Move from 14 to 5: 45 fuel
This costs a total of 168 fuel. This is the new cheapest possible outcome; the old alignment position (2) now costs 206 fuel instead.

Determine the horizontal position that the crabs can align to using the least fuel possible so they can make you an escape route! How much fuel must they spend to align to that position?

 */