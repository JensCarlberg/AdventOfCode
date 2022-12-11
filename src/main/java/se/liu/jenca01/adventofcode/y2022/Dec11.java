package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigInteger;
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
            var operation = line.substring(line.indexOf("=") + 1).strip();
            line = lines.get(lineCount++);
            var test = new BigInteger(line.substring(line.indexOf(" by ") + 4));
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

    private List<BigInteger> getItems(String line) {
        var itemsS = line.substring(line.indexOf(":")+1).split(",");
        var items = new ArrayList<BigInteger>();
        for (int i=0; i<itemsS.length; i++)
            items.add(new BigInteger(itemsS[i].strip()));
        return items;
    }

    public long solve1(Stream<String> stream) {
        return solve(stream, 20, new BigInteger("3"));
    }

    public long solve(Stream<String> stream, int roundCount, BigInteger worryDiv) {
        var monkeys = convertData(stream);
        var monkeyHandleCounts = new TreeMap<Integer, Integer>();
        for (var monkey: monkeys.keySet())
            monkeyHandleCounts.put(monkey, 0);
        for (int round = 0; round < roundCount; round++) {
            if (round > 0 && round % 100 == 0)
                System.out.print(".");
            for (var monkeyId: monkeys.keySet())
                monkeyThrows(monkeyId, monkeys.get(monkeyId), monkeys, monkeyHandleCounts, worryDiv);
        }
        List<Integer> mostHandles = monkeyHandleCounts.values().stream().sorted((a, b) -> b.compareTo(a)).toList();
        Integer t1 = mostHandles.get(0);
        Integer t2 = mostHandles.get(1);
        return 1L * t1 * t2;
    }

    private void monkeyThrows(int id, Monkey monkey, Map<Integer, Monkey> monkeys, Map<Integer, Integer> monkeyHandleCounts, BigInteger worryDiv) {
        for (BigInteger worry: monkey.items) {
            addMonkeyHandleCount(id, monkeyHandleCounts);
            var newWorry = calcWorry(worry, monkey.operation).divideAndRemainder(worryDiv)[0];
            if (newWorry.mod(monkey.test).equals(BigInteger.ZERO)) {
                int target = monkey.trueTarget;
                throwTo(monkeys.get(target), newWorry);
            } else {
                int target = monkey.falseTarget;
                throwTo(monkeys.get(target), newWorry);
            }
        }
        monkey.items.clear();
    }

    private void throwTo(Monkey monkey, BigInteger newWorry) {
        monkey.items.add(newWorry);
    }

    private void addMonkeyHandleCount(int id, Map<Integer, Integer> monkeyHandleCounts) {
        monkeyHandleCounts.put(id, monkeyHandleCounts.get(id) + 1);
    }

    private BigInteger calcWorry(BigInteger worry, String operation) {
        if (operation.contains("*"))
            return multWorry(worry, operation.substring(operation.indexOf("*")));
        if (operation.contains("+"))
            return addWorry(worry, operation.substring(operation.indexOf("+")));
        throw new RuntimeException("Failed calculating for " + operation);
    }

    private BigInteger multWorry(BigInteger worry, String operation) {
        if (operation.contains("old"))
            return worry.multiply(worry);
        var factor = new BigInteger(operation.substring(operation.indexOf("*") + 1).strip());
        return worry.multiply(factor);
    }

    private BigInteger addWorry(BigInteger worry, String operation) {
        if (operation.contains("old"))
            return worry.add(worry);
        String unstripped = operation.substring(operation.indexOf("*") + 2);
        String stripped = unstripped.strip();
        var factor = new BigInteger(stripped);
        return worry.add(factor);
    }

    public long solve2(Stream<String> stream) {
        return solve(stream, 10000, BigInteger.ONE);
    }

    record Monkey(List<BigInteger> items, String operation, BigInteger test, int trueTarget, int falseTarget) implements Comparable<Monkey> {

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

    static class Mod {
        static boolean mod(BigInteger num, int mod) {
            switch (mod) {
                case 1: return true;
                case 2: return mod2(num);
                case 3: return mod3(num);
                case 4: return mod4(num);
                case 5: return mod5(num);
                case 6: return mod2(num) && mod3(num);
                case 7: return mod7(num);
                case 8: return mod8(num);
                case 9: return mod9(num);
                case 10: return mod2(num) && mod5(num);
                case 11: return mod11(num);
                case 12: return mod12(num);
                case 13: return mod13(num);
                case 14: return mod2(num) && mod7(num);
                case 15: return mod3(num) && mod5(num);
                case 16: return mod16(num);
                case 17: return mod17(num);
                case 18: return mod2(num) && mod9(num);
                case 19: return mod19(num);
                case 20: return mod4(num) && mod5(num);
                case 21: return mod3(num) && mod7(num);
                case 22: return mod2(num) && mod11(num);
                case 23: return mod23(num);
            }
        }

        private static boolean mod2(BigInteger num) {
            var numS = num.toString();
            var numC = numS.substring(numS.length() - 1);
            return "02468".contains(numC);
        }

        private static boolean mod3(BigInteger num) {
            var sum = 0;
            for (var c: num.toString().toCharArray())
                sum += c - '0';
            return sum % 3 == 0;
        }

        private static boolean mod4(BigInteger num) {
            var numS = num.toString();
            var numC = numS.substring(numS.length() - 2);
            return Integer.parseInt(numC) % 4 == 0;
        }

        private static boolean mod5(BigInteger num) {
            var numS = num.toString();
            var numC = numS.substring(numS.length() - 1);
            return "05".contains(numC);
        }

        private static boolean mod7(BigInteger num) {
            var numS = num.toString();
            var digits = numS.length();
            var sum = 0;
            var sign = 1;
            for (int pos = digits; pos > 2; pos =- 3) {
                var part = xDigits(numS, pos-3, 3);
                sum += Integer.parseInt(part) * sign;
                sign *= -1;
            }
            return sum % 7 == 0;
        }

        private static boolean mod8(BigInteger num) {
            var numS = num.toString();
            var digits = numS.length();
            if (digits < 3)
                return Integer.parseInt(numS) % 8 == 0;
            var last3 = numS.substring(digits - 3);
            if ("02468".contains(last3.substring(0,1)))
                return Integer.parseInt(numS.substring(1)) % 8 == 0;
            return (Integer.parseInt(numS.substring(1)) + 4) % 8 == 0;
        }

        private static boolean mod9(BigInteger num) {
            var sum = 0;
            for (var c: num.toString().toCharArray())
                sum += c - '0';
            return sum % 9 == 0;
        }

        private static String xDigits(String numS, int pos, int x) {
            while (pos < 0)  {
                pos++;
                x--;
            }
            return numS.substring(pos, pos+x);
        }
    }
}

/*



 */