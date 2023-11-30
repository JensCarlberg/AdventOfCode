package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec21 extends Christmas {

    long sampleAnswer1 = 152;
    long sampleAnswer2 = 301;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec21();
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

    private Map<String,Monkey> convertData(Stream<String> data) {
    	var lines = data.toList();
    	var puzzle = new TreeMap<String, Monkey>();
    	for (var line: lines) {
    		var monkey = buildMonkey(line);
    		puzzle.put(monkey.name, monkey);
    	}
    	return puzzle;
    }

    private Monkey buildMonkey(String line) {
    	var parts = line.split(": ");
    	var operands = parts[1].split(" ");
    	if (operands.length == 1)
    		return Monkey.leaf(parts[0], Integer.parseInt(parts[1].strip()));
    	var left = operands[0].strip();
    	var right = operands[2].strip();
    	switch (operands[1].strip()) {
    	case "+": return Monkey.node(parts[0], left, right, Op.PLUS);
    	case "-": return Monkey.node(parts[0], left, right, Op.MINUS);
    	case "*": return Monkey.node(parts[0], left, right, Op.MULT);
    	case "/": return Monkey.node(parts[0], left, right, Op.DIV);
    	default: throw new RuntimeException("Unknown op: " + operands[1]);
    	}
	}

	public long solve1(Stream<String> stream) {
		var puzzle = convertData(stream);
		var start = puzzle.get("root");
        return start.calc(puzzle);
    }

    public long solve2(Stream<String> stream) {
		var puzzle = convertData(stream);
		var start = puzzle.get("root");
		var myGuess = 16384;
		var low = 0;
		puzzle.put("humn", Monkey.leaf("humn", low));
		var calcLeftLow = puzzle.get(start.left).calc(puzzle);
		var calcRightLow = puzzle.get(start.right).calc(puzzle);
		puzzle.put("humn", Monkey.leaf("humn", low+1));
		var calcLeftHigh = puzzle.get(start.left).calc(puzzle);
		var calcRightHigh = puzzle.get(start.right).calc(puzzle);
		if (calcLeftHigh == calcLeftLow)
			return search(start.right, calcLeftHigh, dir(calcRightHigh, calcRightLow), puzzle);
		if (calcRightHigh == calcRightLow)
			return search(start.left, calcRightHigh, dir(calcLeftHigh, calcLeftLow), puzzle);
		throw new RuntimeException("Cannot find humn makes a difference.");
    }

    private long search(String monkey, long otherResult, int dir, Map<String, Monkey> puzzle) {
    	// Try a modded binary search
		var humnGuess = 16384;
		var lower = 0;
		var upper = 0;
		var lastGuess = 1;
		var humnResult = puzzle.get(monkey).calc(puzzle);
		while (humnResult != otherResult)  {
			System.out.println(humnGuess);
			var dirGuess = dir(humnResult, otherResult);
			if (dirGuess == 0) break;
			if (dirGuess == dir) {
				lower = humnGuess;
				if (upper == 0)
					humnGuess = humnGuess * 2;
				else
					humnGuess = humnGuess + (upper - humnGuess) / 2;
			} else {
				upper = humnGuess;
				humnGuess = humnGuess - (humnGuess - lower) / 2;
			}
			puzzle.put("humn", Monkey.leaf("humn", humnGuess));
			humnResult = puzzle.get(monkey).calc(puzzle);
		}
		return humnGuess;
	}

	private int dir(long high, long low) {
    	var diff = high - low;
    	if (diff == 0) return 0;
    	if (diff < 0) return -1;
    	return 1;
	}

	enum Op { PLUS, MINUS, MULT, DIV }
    record Monkey(String name, String left, String right, Op op, long value) implements Comparable<Monkey> {
    	@Override public int compareTo(Monkey o) {
    		return Objects.compare(this, o, Comparator.comparing(Monkey::name));
    	}
    	boolean isLeaf() { return op == null; }
    	static Monkey leaf(String name, long value) { return new Monkey(name, null, null, null, value); }
    	static Monkey node(String name, String left, String right, Op op) { return new Monkey(name, left, right, op, 0); }
    	long calc(Map<String, Monkey> monkeys) {
    		if (isLeaf()) return value;
    		long calcL = monkeys.get(left).calc(monkeys);
    		long calcR = monkeys.get(right).calc(monkeys);
			switch(op) {
			case PLUS: return calcL + calcR;
			case MINUS: return calcL - calcR;
			case MULT: return calcL * calcR;
			case DIV: {
				var res = calcL / calcR;
				var resD = calcL / (1.0 * calcR);
				if (Math.abs(resD - res) > 0.000001)
					System.out.println(resD);
				return calcL / calcR;
			}
    		}
    		throw new RuntimeException(String.format("Cannot calc. %s %s %s %s %s", name, left, right, op, value));
    	}
    }
}

/*



*/