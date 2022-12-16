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

public class Dec16 extends Christmas {

    long sampleAnswer1 = 1651;
    long sampleAnswer2 = 0;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec16();
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

    private Map<String, Node> convertData(Stream<String> data) {
        var lines = data.toList();
        var puzzle = new TreeMap<String, Node>();
        for (var line: lines) {
            var name = line.split(" ")[1];
            var valve = Integer.parseInt(line.split(";")[0].split("=")[1]);
            var nodes = getConnectedNodes(line.split(";")[1]);
            puzzle.put(name, new Node(name, valve, false, nodes));
        }
        return puzzle;
    }

    private List<String> getConnectedNodes(String connectedDesc) {
        var nodes = new ArrayList<String>();
        for (var p: connectedDesc.split(" "))
                if (p.equals(p.toUpperCase()))
                    nodes.add(p);
        return nodes;
    }

    public long solve1(Stream<String> stream) {
        var puzzle = convertData(stream);
        var pos = puzzle.get("AA");
        var time = 30;
        var totalFlow = 0;
        List<Node> path = new ArrayList<>();
        while (true) {
            if (path.size() == 0 && pos.flow > 0 && !pos.open) {
                pos.open = true;
                totalFlow += calcFlow(puzzle);
                if (--time <= 0) break;
                continue;
            }
            if (path.size() == 0) {
                path = calcPath(pos, puzzle, time);
                if (path.size() == 0 && --time <= 0) break;
                continue;
            }
            if (path.size() > 0) {
                pos = path.remove(0);
                if (--time <= 0) break;
                continue;
            }
        }
        return totalFlow;
    }

    private List<Node> calcPath(Node pos, Map<String, Node> puzzle, int time) {
        // TODO Auto-generated method stub
        return new ArrayList<>();
    }

    private int calcFlow(Map<String, Node> puzzle) {
        return puzzle.values().stream().filter(n -> n.open).mapToInt(n -> n.flow).sum();
    }

    public long solve2(Stream<String> stream) {
        return 0;
    }
    
    class Node implements Comparable<Node> {
        private String name;
        private int flow;
        private boolean open;
        private List<String> connected;

        Node(String name, int flow, boolean open, List<String> connected) {
            this.name = name;
            this.flow = flow;
            this.open = open;
            this.connected = connected;
        }

        String name() { return name; }
        @Override
        public int compareTo(Node o) {
            return Objects.compare(this, o, Comparator.comparing(Node::name));
        }
    }
}

/*



*/