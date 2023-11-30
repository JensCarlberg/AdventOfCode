package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;
import se.liu.jenca01.adventofcode.utils.SetUtils;

public class Dec16 extends Christmas {

	long sampleAnswer1 = 1651;
	long sampleAnswer2 = 1707;
    boolean solve1 = false;
    boolean solve2 = true;
    boolean solveMy = false;

	public static void main(String[] args) throws Exception {
		var christmas = new Dec16();
		christmas.solveSample();
		christmas.solveMy();
	}

	@Override
	public void solveSample() throws Exception {
	    if (solve1) solveSample1();
	    if (solve2) solveSample2();
	}

    private void solveSample2() throws Exception {
		long start = System.currentTimeMillis();
		long solve2 = solve2(sampleData());
		long stop = System.currentTimeMillis();
		System.out.println("Example solve2 timing: " + (stop-start) + "ms");
		assertEquals(sampleAnswer2, solve2, "solve2 is expected to return " + sampleAnswer2);
    }

    private void solveSample1() throws Exception {
        long start = System.currentTimeMillis();
		long solve1 = solve1(sampleData());
		long stop = System.currentTimeMillis();
		System.out.println("Example solve1 timing: " + (stop-start) + "ms");
		assertEquals(sampleAnswer1, solve1, "solve1 is expected to return " + sampleAnswer1);
    }

	@Override
	public void solveMy() throws Exception {
	    if (!solveMy) return;
	    if (solve1) System.out.println(simpleClassName() + " solve1: " + solve1(myData()));
	    if (solve2) System.out.println(simpleClassName() + " solve2: " + solve2(myData()));
	}

	private Puzzle convertData(Stream<String> data) {
		var lines = data.toList();
		var nodes = new TreeMap<String, Node>();
		for (var line: lines) {
			var name = line.split(" ")[1];
			var valve = Integer.parseInt(line.split(";")[0].split("=")[1]);
			var connectedNodes = getConnectedNodes(line.split(";")[1]);
			nodes.put(name, new Node(name, valve, connectedNodes));
		}
		var distances = new TreeMap<Node, Map<Node, Integer>>();
		for (var n: nodes.values()) {
		    distances.put(n, findDistances(n, nodes));
		}
		return new Puzzle(nodes, distances);
	}

	private Map<Node, Integer> findDistances(Node fromNode, TreeMap<String, Node> nodes) {
        var distances = new TreeMap<Node, Integer>();
	    for (var toNode: nodes.values())
	        if (fromNode != toNode)
	            distances.put(toNode, findDistance(fromNode.name, toNode.name, nodes));
        return distances;
    }

    private int findDistance(String fromNode, String targetNode, TreeMap<String, Node> nodes) {
        var visited = new TreeSet<String>();
        visited.add(fromNode);
        var distance = 0;
        while (true) {
            distance++;
            var newVisited = new TreeSet<String>();
            for (var from: visited)
                for (var to: nodes.get(from).connected) {
                    if (to.equals(targetNode))
                        return distance;
                    newVisited.add(to);
                }
            if (newVisited.size() == nodes.size())
                throw new RuntimeException(String.format("No route between nodes. from=%s, to=%s", fromNode, targetNode));
            if (SetUtils.equals(visited, newVisited))
                throw new RuntimeException(String.format("No route between nodes, nodes form multiple subnets. from=%s, to=%s", fromNode, targetNode));
            visited = newVisited;
        }
    }

    private List<String> getConnectedNodes(String connectedDesc) {
		var nodes = new ArrayList<String>();
		for (var p: connectedDesc.split(" "))
			if (p.strip().length() > 0 && p.equals(p.toUpperCase()))
				nodes.add(p.replace(",", ""));
		return nodes;
	}

	public long solve1(Stream<String> stream) {
		var puzzle = convertData(stream);
		var pos = puzzle.nodes.get("AA");
		var time = 30;
		var states = new TreeSet<State>();
		states.add(new State(List.of(pos), Set.of(), 0, State.calcKey(List.of(pos), Set.of())));
		var currMax = 0;
		var maxFlowPerRound = puzzle.nodes.values().stream().mapToInt(Node::flow).sum();
		for (int i=0; i<time; i++) {
		    var maxRestFlow = (time - i) * maxFlowPerRound;
		    System.out.println(String.format("Round %2d: stateCount=%d", (i+1), states.size()));
			var newStates = new TreeSet<State>();
			states1: for (var s: states) {
			    if (s.totalFlow + maxRestFlow < currMax)
			        // This state is too far behind the curve, abandon this state.
			        continue states1;
                var maxExtraFlow = getMaxExtraFlow(getClosedNodes(puzzle.nodes.values(), s.openNodes), 30 - i);
                if (s.totalFlow + maxExtraFlow < currMax)
                    // This state is too far behind the curve, abandon this state.
                    continue states1;
			    states2: for (var s2: states) {
			        if (s == s2) continue states2;
			        if (s.positions.equals(s2.positions) && s.openNodes.equals(s2.openNodes) && s.totalFlow < s2.totalFlow)
			            // There is another identical set of positions and open valves with more flow already, abandon this state.
			            continue states1;
			    }
				newStates.add(s.waitOne()); // Doing nothing is always an option
				if (s.positions.get(0).flow > 0 && !s.openNodes.contains(s.positions.get(0)))
					newStates.add(s.moveAndOpen(s.positions, Set.of(s.positions.get(0))));
				for (var n: s.positions.get(0).connected)
					newStates.add(s.moveAndOpen(List.of(puzzle.nodes.get(n)), Set.of()));
			}
			states = newStates;
			currMax = states.stream().mapToInt(s -> s.totalFlow).max().getAsInt();
		}
		return currMax;
	}

	private int getMaxExtraFlow(Collection<Node> closedNodes, int remainingRounds) {
        return remainingRounds * Node.flowFrom(closedNodes);
    }

    private Collection<Node> getClosedNodes(Collection<Node> allNodes, Collection<Node> openNodes) {
        var closedNodes = new TreeSet<Node>();
        closedNodes.addAll(Set.copyOf(allNodes));
        closedNodes.removeAll(openNodes);
        return Set.copyOf(closedNodes);
    }

    public long solve2(Stream<String> stream) {
        var puzzle = convertData(stream);
        var pos = puzzle.nodes.get("AA");
        var time = 26;
        var states = new TreeSet<State>();
        states.add(new State(List.of(pos, pos), Set.of(), 0, State.calcKey(List.of(pos, pos), Set.of())));
        var currMax = 0;
        var maxFlowPerRound = puzzle.nodes.values().stream().mapToInt(Node::flow).sum();
        for (int i=0; i<time; i++) {
            var maxRestFlow = (time - i) * maxFlowPerRound;
            System.out.println(String.format("Round %2d: stateCount=%d", (i+1), states.size()));
            var newStates = new TreeSet<State>();
            states1: for (var s: states) {
                if (s.totalFlow + maxRestFlow < currMax)
                    // This state is too far behind the curve, abandon this state.
                    continue states1;
//                var maxExtraFlow = getMaxExtraFlow(getClosedNodes(puzzle.nodes.values(), s.openNodes), time - i);
                var maxExtraFlow = flowFromClosedNodes(
                        getClosedNodes(puzzle.nodes.values(), s.openNodes),
                        s.positions,
                        time - i,
                        puzzle.distances);
                if (s.totalFlow + maxExtraFlow < currMax)
                    // This state is too far behind the curve, abandon this state.
                    continue states1;
//                states2: for (var s2: states) {
//                    if (s == s2) continue states2;
//                    if (s.positions.equals(s2.positions) && s.openNodes.equals(s2.openNodes) && s.totalFlow < s2.totalFlow)
//                        // There is another identical set of positions and open valves with more flow already, abandon this state.
//                        continue states1;
//                }
                newStates.add(s.waitOne()); // Doing nothing is always an option
                var p0 = s.positions.get(0);
                var p1 = s.positions.get(1);
                // open 0,
                if (p0.flow > 0 && !s.openNodes.contains(p0)) {
                    // open 1
                    if (p1.flow > 0 && !s.openNodes.contains(p1)) {
                        newStates.add(s.moveAndOpen(s.positions, s.positions));
                    }
                    // move 1
                    for (var n: p1.connected)
                        newStates.add(s.moveAndOpen(List.of(p0, puzzle.nodes.get(n)), Set.of(p0)));
                }
                // open 1,
                if (p1.flow > 0 && !s.openNodes.contains(p1)) {
                    // move 0
                    for (var n: p0.connected)
                        newStates.add(s.moveAndOpen(List.of(p1, puzzle.nodes.get(n)), Set.of(p1)));
                }
                // move 0,
                for (var n0: p0.connected)
                    // move 1
                    for (var n1: p1.connected)
                        newStates.add(s.moveAndOpen(List.of(puzzle.nodes.get(n0), puzzle.nodes.get(n1)), Set.of()));
            }
            states = newStates;
            currMax = states.stream().mapToInt(s -> s.totalFlow).max().getAsInt();
        }
        return currMax;
	}

    static int flowFromClosedNodes(Collection<Node> closedNodes, Collection<Node> reachableFrom, int roundsLeft, Map<Node, Map<Node, Integer>> distances) {
        int totalFlow = 0;
        for (var n: closedNodes) {
            if (n.flow == 0) continue;
            var nDistances = distances.get(n);
            if (reachableFrom.contains(n)) continue;
            var minDist = Integer.MAX_VALUE;
            for (var from: reachableFrom)
                minDist = Math.min(minDist, nDistances.get(from));
            if (roundsLeft - 1 - minDist <= 0) continue;
            totalFlow += n.flow * (roundsLeft - 1 - minDist);
        }
        return totalFlow;
    }

	record State(List<Node> positions, Set<Node> openNodes, int totalFlow, String key) implements Comparable<State> {
		@Override
		public int compareTo(State o) {
		    return Objects.compare(this, o, Comparator
		            .comparing(State::totalFlow)
		            .thenComparing(State::key));
		}
		State moveAndOpen(List<Node> newPositions, Collection<Node> newOpens) {
		    var nodes = new TreeSet<Node>();
		    nodes.addAll(openNodes);
		    nodes.addAll(newOpens);
		    return new State(
		            newPositions.stream().sorted().toList(),
		            Set.copyOf(nodes),
		            totalFlow + Node.flowFrom(openNodes),
		            calcKey(newPositions, nodes));
		}
		State waitOne() { return moveAndOpen(positions, Set.of()); }
		static String calcKey(List<Node> positions, Set<Node> openNodes) {
		    return calcKey(positions) + "#" + calcKey(openNodes);
		}
		static String calcKey(Collection<Node> nodes) { return String.join(",", nodes.stream().sorted().map(n -> n.name).toList().toString()); }
	}
	record Node(String name, int flow, List<String> connected) implements Comparable<Node> {
		@Override public int compareTo(Node o) { return name.compareTo(o.name); }
		static int flowFrom(Collection<Node> nodes) { return nodes.stream().mapToInt(n -> n.flow).sum(); }
	}
	record Puzzle(Map<String, Node> nodes, Map<Node, Map<Node, Integer>> distances) {}
}

/*



 */