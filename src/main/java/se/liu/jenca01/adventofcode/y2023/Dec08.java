package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec08 extends Christmas {

    long sampleAnswer1 = 2;
    long sampleAnswer2 = 6;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec08();
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
        long solve2 = solve2(sampleData(2));
        stop = System.currentTimeMillis();
        System.out.println("Example solve2 timing: " + (stop-start) + "ms");
        assertEquals(sampleAnswer2, solve2, "solve2 is expected to return " + sampleAnswer2);
    }

    @Override
    public void solveMy() throws Exception {
        System.out.println(simpleClassName() + " solve1: " + solve1(myData()));
        System.out.println(simpleClassName() + " solve2: " + solve2(myData()));
    }

    private Pair<String, Map<String, Pair<String, String>>> convertData(Stream<String> data) {
        var lines = data.toList();

        var directions = lines.get(0);
        var nodes = new TreeMap<String, Pair<String, String>>();
        for (int i=2; i<lines.size(); i++) {
            var line = lines.get(i);
            parseLine(line, nodes);
        }
        return Pair.of(directions, nodes);
    }

    private void parseLine(String line, TreeMap<String, Pair<String, String>> nodes) {
        // 0123456789012345
        // AAA = (BBB, CCC)
        var name = line.substring(0, 3);
        var left = line.substring(7, 10);
        var right = line.substring(12, 15);
        nodes.put(name,  Pair.of(left, right));
    }

    public long solve1(Stream<String> stream) {
        var data = convertData(stream);
        var directions = data.getLeft();
        var nodes = data.getRight();
        String start = "AAA";
        List<String> ends = List.of("ZZZ");
        return solve(directions, nodes, start, ends);
    }

    public long solve(String directions, Map<String, Pair<String, String>> nodes, String start, List<String> ends) {
        long steps = 0;
        int dirPos = 0;
        String currentPos = start;
        while (true) {
            var youAreHere = nodes.get(currentPos);
            var dir = directions.charAt(dirPos);
            steps++;
            dirPos = (dirPos + 1) % directions.length();
            switch(dir) {
            case 'L': currentPos = youAreHere.getLeft(); break;
            case 'R': currentPos = youAreHere.getRight(); break;
            default: throw new RuntimeException();
            }
            if (ends.contains(currentPos)) break;
        }
        return steps;
    }

    public long solve2(Stream<String> stream) {
        var data = convertData(stream);
        var directions = data.getLeft();
        var nodes = data.getRight();
        var startPos = getAllEndingIn('A', nodes);
        var endPos = getAllEndingIn('Z', nodes);
        var steps = new ArrayList<Long>();

        for (var start: startPos)
            steps.add(solve(directions, nodes, start, endPos));


        return lcm2(steps);
    }

    private Stream<String> getCurrentPosNodes(Map<String, Pair<String, String>> nodes, List<String> currentPos) {
        return nodes.keySet().stream().filter(n -> currentPos.contains(n));
    }

    private List<String> getAllEndingIn(char endsIn, Map<String, Pair<String, String>> nodes) {
        return nodes.keySet().stream().filter(n -> endsIn == n.charAt(2)).toList();
    }

    private List<String> getAllNotEndingInZ(List<String> nodeNames) {
        return nodeNames.stream().filter(n -> 'Z' != n.charAt(2)).toList();
    }

    private long lcm(List<Long> numbers) {
        System.out.print("Testar");
        for (long num: numbers)
            System.out.print(" " + num);
        System.out.println();
        long lcmGuess = 2;
        while (true) {
            boolean found = true;
            for (long num: numbers)
                if (lcmGuess % num != 0) {
                    found = false;
                    break;
                }
            if (found) {
                System.out.println();
                return lcmGuess;
            }
            lcmGuess++;
            if (lcmGuess % 1000000000 == 0) System.out.print(".");
            if (lcmGuess % 10000000000L == 0) System.out.println();
        }
    }

    private long lcm2(List<Long> numbers) {
        long max = numbers.stream().mapToLong(l -> l).max().getAsLong();
        var primes = new ArrayList<Long>();
        primes.add(2L);
        primes: for (long l = 3; l < Math.sqrt(max); l++) {
            for (var p: primes)
                if (l % p == 0) continue primes;
            primes.add(l);
        }
        var factors = new TreeMap<Long, List<Long>>();
        for (var num: numbers)
            factors.put(num, factors(num, primes));
        return 6;
    }

    private List<Long> factors(long num, List<Long> primes) {
        var factors = new ArrayList<Long>();
        for (int i=0 ; i<primes.size(); ) {
            Long prime = primes.get(i);
            if (num % prime == 0) {
                factors.add(prime);
                num /= prime;
            } else {
                i++;
            }
        }
        if (num != 1)
            factors.add(num);
        return factors;
    }

    record Node(String name, Pair<String, String> connectedNodes) {}
}
