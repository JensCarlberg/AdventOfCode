package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        String currentPos = start;
        String end = "ZZZ";
        int steps = 0;
        while (true) {
            var youAreHere = nodes.get(currentPos);
            var dir = directions.charAt(steps++ % directions.length());
            switch(dir) {
            case 'L': currentPos = youAreHere.getLeft(); break;
            case 'R': currentPos = youAreHere.getRight(); break;
            default: throw new RuntimeException();
            }
            if (currentPos.equals(end)) break;
        }
        return steps;
    }

    public long solve2(Stream<String> stream) {
        var data = convertData(stream);
        var directions = data.getLeft();
        var nodes = data.getRight();
        int steps = 0;
        List<String> currentPos = getAllEndingInA(nodes);

        while (true) {
            var dir = directions.charAt(steps++ % directions.length());
            var youAreHere = getCurrentPosNodes(nodes, currentPos);
            var nextPos = youAreHere
                    .map(n -> dir == 'L' ? nodes.get(n).getLeft() : nodes.get(n).getRight())
                    .toList();

            currentPos = nextPos;

            var notAnExit = getAllNotEndingInZ(nextPos);
            if (notAnExit.size() == 0) break;
            if (steps % 10000 == 0) System.out.print(".");
            if (steps % 1000000 == 0) System.out.println();
        }
        System.out.println();

        return steps;
    }

    private Stream<String> getCurrentPosNodes(Map<String, Pair<String, String>> nodes, List<String> currentPos) {
        return nodes.keySet().stream().filter(n -> currentPos.contains(n));
    }

    private List<String> getAllEndingInA(Map<String, Pair<String, String>> nodes) {
        return nodes.keySet().stream().filter(n -> 'A' == n.charAt(2)).toList();
    }

    private List<String> getAllNotEndingInZ(List<String> nodeNames) {
        return nodeNames.stream().filter(n -> 'Z' != n.charAt(2)).toList();
    }

    record Node(String name, Pair<String, String> connectedNodes) {}
}
