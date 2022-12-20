package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec20 extends Christmas {

    long sampleAnswer1 = 3;
    long sampleAnswer2 = 1623178306;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec20();
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

    private List<Num> convertData(Stream<String> data, long multiplier) {
    	var puzzle = new ArrayList<Num>();
    	var lines = data.toList();
    	for (var i=0; i<lines.size(); i++)
    		puzzle.add(new Num(i, multiplier * Integer.parseInt(lines.get(i))));
    	return puzzle;
    }

    public long solve1(Stream<String> stream) {
    	var puzzle = convertData(stream, 1);
    	var mixed = new ArrayList<Num>();
    	mixed.addAll(puzzle);
    	var ms = new TreeSet<Num>();
    	ms.addAll(puzzle);
    	if (ms.size() != mixed.size()) {
    		for (var i: ms)
    			mixed.remove(mixed.indexOf(i));
    		System.out.println(mixed);
    		throw new RuntimeException("Found doubles...");
    	}
    	for(var num: puzzle) {
    		if (num.value == 0) continue;
    		var pos = mixed.indexOf(num);
    		mix(pos, mixed);
    	}
    	var pos0 = 0;
    	for (; pos0 < mixed.size(); pos0++) {
    		if (mixed.get(pos0).value == 0)
    			break;
    	}
        int pos1 = (pos0 + 1000) % mixed.size();
        int pos2 = (pos0 + 2000) % mixed.size();
        int pos3 = (pos0 + 3000) % mixed.size();
        long val1 = mixed.get(pos1).value;
        long val2 = mixed.get(pos2).value;
        long val3 = mixed.get(pos3).value;
		return val1 + val2 + val3;
    }

    private void mix(int pos, ArrayList<Num> mixed) {
    	Num  posNum  = mixed.remove(pos);
    	var newPos = (pos + posNum.value) % mixed.size();
    	if (newPos < 0) newPos += mixed.size();
    	mixed.add((int) newPos, posNum);
	}

	public long solve2(Stream<String> stream) {
    	var puzzle = convertData(stream, 811589153);
    	var mixed = new ArrayList<Num>();
    	mixed.addAll(puzzle);
    	var ms = new TreeSet<Num>();
    	for (int i=0; i<10; i++) {
    		for(var num: puzzle) {
    			if (num.value == 0) continue;
    			var pos = mixed.indexOf(num);
    			mix(pos, mixed);
    		}
    	}
    	var pos0 = 0;
    	for (; pos0 < mixed.size(); pos0++) {
    		if (mixed.get(pos0).value == 0)
    			break;
    	}
        int pos1 = (pos0 + 1000) % mixed.size();
        int pos2 = (pos0 + 2000) % mixed.size();
        int pos3 = (pos0 + 3000) % mixed.size();
        long val1 = mixed.get(pos1).value;
        long val2 = mixed.get(pos2).value;
        long val3 = mixed.get(pos3).value;
		return val1 + val2 + val3;
    }

	record Num(int pos, long value) implements Comparable<Num> {
		@Override public int compareTo(Num o) {
			return Objects.compare(this, o, Comparator.comparing(Num::pos).thenComparing(Num::value));
		}
		@Override public String toString() { return String.format("{%s: %s}", pos, value); }
	}
}

/*



*/