package se.liu.jenca01.adventofcode.y2024;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec05 extends Christmas {

    long sampleAnswer1 = 143;
    long sampleAnswer2 = 123;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec05();
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

    private List<String> convertData(Stream<String> data) {
        return data.toList();
    }

    private Map<Long, Set<Long>> getRules(List<String> lines) {
    	var rules = new HashMap<Long, Set<Long>>();
    	for (var line: lines) {
    		if (line.isBlank()) break;
    		var parts = line.split("\\|");
    		addRule(rules, parts[0], parts[1]);
    	}
    	return rules;
    }

    private void addRule(HashMap<Long, Set<Long>> rules, String before, String after) {
    	var pageBefore = Long.parseLong(before);
    	var pageAfter = Long.parseLong(after);
    	var pagesBefore = rules.get(pageAfter);
    	if (pagesBefore == null) pagesBefore = new TreeSet<Long>();
    	pagesBefore.add(pageBefore);
    	rules.put(pageAfter, pagesBefore);
    }

    private List<List<Long>> getUpdates(List<String> lines) {
    	var updates = new ArrayList<List<Long>>();
    	var readPastRules = false;
    	for (var line: lines) {
    		readPastRules = line.isBlank() || readPastRules;
    		if (!readPastRules || line.isBlank())
    			continue;
    		var parts = line.split(",");
    		updates.add(Arrays.stream(parts).map(p -> Long.parseLong(p)).toList());
    	}
    	return updates;
    }

    public long solve1(Stream<String> stream) {
    	var lines = convertData(stream);
    	var rules = getRules(lines);
    	var updates = getUpdates(lines);

    	var printable = updates.stream().filter(u -> isCorrectPrintingOrder(u, rules)).toList();
    	var pageSum = 0L;
    	for (var update : printable)
    		pageSum += update.get((update.size() - 1) / 2);
        return pageSum;
    }

	private boolean isCorrectPrintingOrder(List<Long> update, Map<Long, Set<Long>> rules) {
		for(int i=0; i<update.size()-1; i++) {
			var page = update.get(i);
			var pagesThatShouldGoBefore = rules.get(page);
			for(int j=i+1; j<update.size(); j++) {
				var laterPageInUpdate = update.get(j);
				if (pagesThatShouldGoBefore != null && pagesThatShouldGoBefore.contains(laterPageInUpdate))
					return false;
			}
		}
		return true;
	}

	public long solve2(Stream<String> stream) {
    	var lines = convertData(stream);
    	var rules = getRules(lines);
    	var updates = getUpdates(lines);

    	var nonPrintable = updates.stream()
    			.filter(u -> !isCorrectPrintingOrder(u, rules))
    			.map(l -> fixOrder(l))
    			.toList();
    	var pageSum = 0L;
    	for (var update : nonPrintable)
    		pageSum += update.get((update.size() - 1) / 2);
        return pageSum;
    }

	private List<Long> fixOrder(List<Long> l) {
		// TODO Auto-generated method stub
		return l;
	}
}
