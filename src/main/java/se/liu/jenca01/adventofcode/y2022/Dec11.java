package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec11 extends Christmas {

    long sampleAnswer1 = 10605;
    long sampleAnswer2 = 2713310158L;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec11();
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

    private Map<Integer, Monkey> convertData(Stream<String> data) {
        var lines = data.toList();
        var monkeys = new TreeMap<Integer, Monkey>();
        for (var lineCount = 0; lineCount < lines.size(); ) {
            var line = lines.get(lineCount++);
            if (line.strip().length() == 0)
                continue;
            var monkeyNum  = Integer.parseInt(line.substring(7, line.indexOf(":")));
            line = lines.get(lineCount++);
            var worryLevels = getItems(line);
            line = lines.get(lineCount++);
            var operation = getOperation(line);
            line = lines.get(lineCount++);
            var test = Integer.parseInt(lineAfter(line, "Test: divisible by "));
            line = lines.get(lineCount++);
            var targetTrue = Integer.parseInt(lineAfter(line, "throw to monkey "));
            line = lines.get(lineCount++);
            var targetFalse = Integer.parseInt(lineAfter(line, "throw to monkey "));
            lineCount++;
            monkeys.put(monkeyNum, new Monkey(worryLevels, operation, test, targetTrue, targetFalse));
        }
        return monkeys;
    }

    private String lineAfter(String line, String match) {
        var matchAdd = match.length();
        var matchPos = line.indexOf(match);
        return line.substring(matchPos + matchAdd);
    }

    private List<Long> getItems(String line) {
        var itemsS = lineAfter(line, "Starting items: ").split(",");
        var items = new ArrayList<Long>();
        for (int i=0; i<itemsS.length; i++)
            items.add(Long.parseLong(itemsS[i].strip()));
        return items;
    }

    private Op getOperation(String opString) {
    	var opS = lineAfter(opString, "Operation: new = old ");
    	var parts = opS.split(" ");
    	var opType = "+".equals(parts[0]) ? OpType.ADD : OpType.MULT;
    	int param = "old".equals(parts[1]) ? 0 : Integer.parseInt(parts[1]);
		return new Op(opType, param);
    }

    public long solve1(Stream<String> stream) {
    	var worryReduction = 3;
    	var rounds = 20;
        return solve(stream, worryReduction, rounds);
    }

	private long solve(Stream<String> stream, int worryReduction, int rounds) {
		var monkeys = convertData(stream);
		var commonMod = calcCommonMod(monkeys);
		var monkeyHandleCounts = new TreeMap<Integer, Integer>();
		for (var monkey: monkeys.keySet())
		    monkeyHandleCounts.put(monkey, 0);
		for (int round = 0; round < rounds; round++) {
		    for (var monkeyId: monkeys.keySet())
		        monkeyThrows(monkeyId, monkeys.get(monkeyId), monkeys, monkeyHandleCounts, worryReduction, commonMod);
		}
		List<Integer> mostHandles = monkeyHandleCounts.values().stream().sorted((a, b) -> b.compareTo(a)).toList();
		Integer t1 = mostHandles.get(0);
		Integer t2 = mostHandles.get(1);
		return 1L * t1 * t2;
	}

    private long calcCommonMod(Map<Integer, Monkey> monkeys) {
    	var mod = 1L;
    	for (var monkey: monkeys.values())
    		mod *= monkey.test;
		return mod;
	}

	private void monkeyThrows(int id, Monkey monkey, Map<Integer, Monkey> monkeys, Map<Integer, Integer> monkeyHandleCounts, int worryDiv, long commonMod) {
        for (var worryItem: monkey.items) {
            addMonkeyHandleCount(id, monkeyHandleCounts);
            switch (monkey.operation.type) {
			case ADD:
				worryItem += monkey.operation.param;
				break;
			case MULT:
				worryItem *= (monkey.operation.param == 0) ? worryItem : monkey.operation.param;
				break;
            }
            worryItem /= worryDiv;
            worryItem %= commonMod;
            if (worryItem % monkey.test == 0) {
                int target = monkey.trueTarget;
                throwTo(monkeys.get(target), worryItem);
            } else {
                int target = monkey.falseTarget;
                throwTo(monkeys.get(target), worryItem);
            }
        }
        monkey.items.clear();
    }

    private void throwTo(Monkey monkey, Long item) {
        monkey.items.add(item);
    }

    private void addMonkeyHandleCount(int id, Map<Integer, Integer> monkeyHandleCounts) {
        monkeyHandleCounts.put(id, monkeyHandleCounts.get(id) + 1);
    }

    public long solve2(Stream<String> stream) {
    	var worryReduction = 1;
    	var rounds = 10000;
        return solve(stream, worryReduction, rounds);
    }

    enum OpType { ADD, MULT }
    record Op(OpType type, int param) implements Comparable<Op> {
    	@Override public String toString() { return String.format("%s: %s", type, param); }
		@Override public int compareTo(Op o) {
			return Objects.compare(this, o, Comparator.comparing(Op::type).thenComparing(Op::param));
		}
    }
    record Monkey(List<Long> items, Op operation, long test, int trueTarget, int falseTarget) implements Comparable<Monkey> {
    	@Override public String toString() {
    		return String.format(""
    				+ "Items: %s\n"
    				+ "Operation: %s\n"
    				+ "Test: %s\n"
    				+ "  true: %s\n"
    				+ "  false: %s\n----\n",
    				items.toString(), operation, test, trueTarget, falseTarget);
    	}
        @Override public int compareTo(Monkey o) {
            return Objects.compare(
                    this,
                    o,
                    Comparator
                    .comparing(Monkey::operation)
                    .thenComparing(Monkey::test)
                    .thenComparing(Monkey::trueTarget)
                    .thenComparing(Monkey::falseTarget)
                    );
        }

    }

}

/*



 */