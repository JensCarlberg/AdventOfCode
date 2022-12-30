package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec23 extends Christmas {

    long sampleAnswer1 = 110;
    long sampleAnswer2 = 20;

    static Pos[][] checks = new Pos[4][];
    static {
    	checks[0] = new Pos[] { new Pos(-1, -1), new Pos(-1,  0), new Pos(-1,  1) }; // N
    	checks[1] = new Pos[] { new Pos( 1, -1), new Pos( 1,  0), new Pos( 1,  1) }; // S
    	checks[2] = new Pos[] { new Pos(-1, -1), new Pos( 0, -1), new Pos( 1, -1) }; // W
    	checks[3] = new Pos[] { new Pos(-1,  1), new Pos( 0,  1), new Pos( 1,  1) }; // E
    }

    public static void main(String[] args) throws Exception {
        var christmas = new Dec23();
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

    private Set<Pos> convertData(Stream<String> data) {
    	var lines = data.toList();
    	var puzzle = new TreeSet<Pos>();
    	for (int row=0; row<lines.size(); row++)
    		for (int col=0; col<lines.get(row).length(); col++)
    			if (lines.get(row).charAt(col) == '#')
    				puzzle.add(new Pos(row, col));
        return puzzle;
    }

    public long solve1(Stream<String> stream) {
    	Collection<Pos> puzzle = convertData(stream);
    	int order = 0;
    	for (int i=0; i<10; i++) {
    		var proposedMoves = propose(puzzle, order);
    		Map<Pos, Pos> actualMoves = removeCollisions(proposedMoves);
			puzzle = actualMoves.values();
    		order = (order + 1) % checks.length;
    	}
        return countSpace(puzzle);
    }

	private Map<Pos, Pos> removeCollisions(Map<Pos, Pos> proposedMoves) {
    	var cleaned = new TreeMap<Pos, Pos>();
    	cleaned.putAll(proposedMoves);
    	for(var pos: proposedMoves.values()) {
    		var elfs = new TreeSet<Pos>();
    		for (var elf: proposedMoves.keySet()) {
    			if (proposedMoves.get(elf).equals(pos))
    				elfs.add(elf);
    		}
    		if (elfs.size() > 1) {
    			for (var elf: elfs) {
    				cleaned.put(elf, elf);
    			}
    		}
    	}
		return cleaned;
	}

	private Map<Pos, Pos> propose(Collection<Pos> puzzle, int order) {
    	var proposedMoves = new TreeMap<Pos, Pos>();
    	for (var elf: puzzle) {
    		var needsMove = false;
    		for (int i=0; i<4; i++)
    			needsMove = needsMove || !canMove(elf, puzzle, (order + i) % checks.length);
    		if (!needsMove) continue;
    		for (int i=0; i<4; i++)
    			if (canMove(elf, puzzle, (order + i) % checks.length)) {
    				proposedMoves.put(elf, elf.plus(checks[(order + i) % checks.length][1]));
    				break;
    			}
    	}

    	var elfNotMoving = new TreeSet<Pos>();
    	elfNotMoving.addAll(puzzle);
    	elfNotMoving.removeAll(proposedMoves.keySet());
    	for (var elf: elfNotMoving)
    		proposedMoves.put(elf, elf);

    	if (puzzle.size() != proposedMoves.size()) {
    		throw new RuntimeException(String.format("Mismatch! %s %s", puzzle.size(), proposedMoves.size()));
    	}
    	return proposedMoves;
	}

	private boolean canMove(Pos elf, Collection<Pos> puzzle, int check) {
		for (var p: checks[check]) {
			if (puzzle.contains(elf.plus(p)))
				return false;
		}
		return true;
	}

	private long countSpace(Collection<Pos> puzzle) {
    	var minR = puzzle.stream().mapToInt(p -> p.row).min().getAsInt();
    	var maxR = puzzle.stream().mapToInt(p -> p.row).max().getAsInt();
    	var minC = puzzle.stream().mapToInt(p -> p.col).min().getAsInt();
    	var maxC = puzzle.stream().mapToInt(p -> p.col).max().getAsInt();

    	return (maxR - minR + 1) * (maxC - minC + 1) - puzzle.size();
	}

	public long solve2(Stream<String> stream) {
    	Collection<Pos> puzzle = convertData(stream);
    	int order = 0;
    	int steps = 0;
    	while (true) {
    		steps++;
    		var proposedMoves = propose(puzzle, order);
    		Map<Pos, Pos> actualMoves = removeCollisions(proposedMoves);
    		if (noMoves(puzzle, actualMoves.values()))
    			return steps;
			puzzle = actualMoves.values();
    		order = (order + 1) % checks.length;
    		System.out.print(".");
    		if (steps % 20 == 0) System.out.println(" - " + steps);
    	}
    }

    private boolean noMoves(Collection<Pos> puzzle, Collection<Pos> values) {
    	if (puzzle.size() != values.size()) throw new RuntimeException();
    	var test = new TreeSet<Pos>();
    	test.addAll(puzzle);
    	test.removeAll(values);
    	if (test.size() != 0) return false;
    	test.clear();
    	test.addAll(values);
    	test.removeAll(puzzle);
    	if (test.size() != 0) return false;
		return true;
	}

	record Pos(int row, int col) implements Comparable<Pos> {
		@Override public int compareTo(Pos o) { return Objects.compare(this,  o,  Comparator.comparing(Pos::row).thenComparing(Pos::col)); }
		Pos plus(Pos o) { return new Pos(row + o.row, col + o.col); }
    }
}

/*



*/