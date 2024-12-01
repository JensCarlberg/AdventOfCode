package se.liu.jenca01.adventofcode.y2024;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec01 extends Christmas {

    long sampleAnswer1 = 11;
    long sampleAnswer2 = 31;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec01();
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

    private List<Pair<Long, Long>> convertData(Stream<String> data) {
        return data.map(s -> parsePair(s)).collect(Collectors.toList());
    }

    private Pair<Long, Long> parsePair(String s) {
    	while (s.contains("  "))
    		s = s.replace("  ", " ");
    	var p = s.split(" ");
    	return Pair.of(Long.parseLong(p[0]), Long.parseLong(p[1]));
	}

	public long solve1(Stream<String> stream) {
		var inPairs = convertData(stream);
		var left = new ArrayList<Long>();
		var right = new ArrayList<Long>();
		for (var p: inPairs) {
			left.add(p.getLeft());
			right.add(p.getRight());
		}
		left.sort(Long::compare);
		right.sort(Long::compare);
		long diff = 0;
		while(left.size()>0)
			diff += Math.abs(left.removeFirst() - right.removeFirst());
        return diff;
    }

    public long solve2(Stream<String> stream) {
    	var inPairs = convertData(stream);
    	var left = new TreeMap<Long, Long>();
    	var right = new TreeMap<Long, Long>();
		for (var p: inPairs) {
			addOne(left, p.getLeft());
			addOne(right, p.getRight());
		}
		long similarity = 0;
		for(var key : left.keySet())
			similarity += key * getKeyOrZero(left, key) * getKeyOrZero(right, key);
        return similarity;
    }

	private void addOne(TreeMap<Long, Long> map, Long key) {
		if (map.containsKey(key))
			map.put(key, map.get(key) + 1);
		else
			map.put(key, 1L);		
	}

	private Long getKeyOrZero(TreeMap<Long, Long> map, Long key) {
		if (map.containsKey(key))
			return map.get(key);
		else
			return 0L;		
	}
}
