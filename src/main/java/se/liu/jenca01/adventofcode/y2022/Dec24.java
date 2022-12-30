package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec24 extends Christmas {

    long sampleAnswer1 = 18;
    long sampleAnswer2 = 54;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec24();
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

    static Pos[] dirs = new Pos[] {
    		new Pos(0, -1),
    		new Pos(0, 1),
    		new Pos(-1, 0),
    		new Pos(1, 0),
    };

    private Puzzle convertData(Stream<String> data) {
    	var rows = data.toList();
    	var walls = new TreeSet<Pos>();
    	var whirlswinds = new TreeSet<Whirlwind>();
    	for (int row=0; row<rows.size(); row++)
    		for (int col=0; col<rows.get(row).length(); col++) {
    			var c = rows.get(row).charAt(col);
    			switch (c) {
				case '.': break;
				case '#': walls.add(new Pos(row, col)); break;
				case '<': whirlswinds.add(new Whirlwind(new Pos(row, col), dirs[0], new Pos(row, rows.get(row).length() - 2))); break;
				case '>': whirlswinds.add(new Whirlwind(new Pos(row, col), dirs[1], new Pos(row, 1))); break;
				case '^': whirlswinds.add(new Whirlwind(new Pos(row, col), dirs[2], new Pos(rows.size() - 2, col))); break;
				case 'v': whirlswinds.add(new Whirlwind(new Pos(row, col), dirs[3], new Pos(1, col))); break;
				default:
					throw new IllegalArgumentException("Unexpected value: " + c);
				}
    		}
    	var start = new Pos(0, 1);
    	var end = new Pos(rows.size() - 1, rows.get(rows.size() - 1).length() - 2);
    	return new Puzzle(start, end, walls, whirlswinds);
    }

    public long solve1(Stream<String> stream) {
    	var puzzle = convertData(stream);
    	return solve(puzzle).steps;
    }

    public PuzzleSolution solve(Puzzle puzzle) {
    	var potPos = new TreeSet<Pos>();
    	var walls = puzzle.walls;
    	var whirlwinds = puzzle.whirlwinds;
    	var minRow = Math.min(puzzle.start.row, puzzle.end.row);
    	var maxRow = Math.max(puzzle.start.row, puzzle.end.row);
    	potPos.add(puzzle.start);
    	var step = 0;
    	while (true) {
    		whirlwinds = move(whirlwinds, walls);
    		potPos = findOpenPos(potPos, whirlwinds, walls, minRow, maxRow);
    		step++;
    		if (potPos.contains(puzzle.end)) return new PuzzleSolution(step, whirlwinds);
    		if (step % 10 == 0) System.out.print(".");
    		if (step % 1000 == 0) System.out.println();
    	}
    }

    private TreeSet<Pos> findOpenPos(TreeSet<Pos> potPos, Set<Whirlwind> whirlwinds, Set<Pos> walls, int minRow, int maxRow) {
    	var newPotPos = new TreeSet<Pos>();
    	var blockedPos = blocked(whirlwinds, walls);
    	for (var pos: potPos) {
    		if (!blockedPos.contains(pos)) newPotPos.add(pos);
    		for (var d: dirs) {
    			var tmpPos = pos.plus(d);
    			if (tmpPos.row < minRow) continue;
    			if (tmpPos.row > maxRow) continue;
    			if (blockedPos.contains(tmpPos)) continue;
    			newPotPos.add(tmpPos);
    		}
    	}
		return newPotPos;
	}

	private Set<Pos> blocked(Set<Whirlwind> whirlwinds, Set<Pos> walls) {
    	var blockedPos = new TreeSet<Pos>();
    	blockedPos.addAll(walls);
    	for (var w: whirlwinds)
    		blockedPos.add(w.pos);
		return blockedPos;
	}

	private Set<Whirlwind> move(Set<Whirlwind> whirlwinds, Set<Pos> walls) {
    	var moved = new TreeSet<Whirlwind>();
    	for (var w: whirlwinds) {
    		var moveTo = w.pos.plus(w.dir);
    		if (walls.contains(moveTo))
    			moveTo = w.reset;
    		moved.add(w.repos(moveTo));
    	}
		return moved;
	}

	public long solve2(Stream<String> stream) {
		var puzzleThere = convertData(stream);
		var solutionThere = solve(puzzleThere);
		var puzzleBack = new Puzzle(puzzleThere.end, puzzleThere.start, puzzleThere.walls, solutionThere.whirlwinds);
		var solutionBack = solve(puzzleBack);
		var puzzleThereAgain = new Puzzle(puzzleBack.end, puzzleBack.start, puzzleBack.walls, solutionBack.whirlwinds);
		var solutionThereAgain = solve(puzzleThereAgain);
        return solutionThere.steps + solutionBack.steps + solutionThereAgain.steps;
    }

    record Pos(int row, int col) implements Comparable<Pos> {
		@Override public int compareTo(Pos o) {
			return Objects.compare(this, o, Comparator.comparing(Pos::row).thenComparing(Pos::col));
		}
		Pos plus(Pos o) { return new Pos(row + o.row, col + o.col); }
    }
    record Whirlwind(Pos pos, Pos dir, Pos reset) implements Comparable<Whirlwind> {
		@Override public int compareTo(Whirlwind o) {
			return Objects.compare(
					this, o,
					Comparator
					.comparing(Whirlwind::pos)
					.thenComparing(Whirlwind::dir)
					.thenComparing(Whirlwind::reset));
		}
		Whirlwind repos(Pos pos) { return new Whirlwind(pos, dir, reset); }
    }
    record Puzzle(Pos start, Pos end, Set<Pos> walls, Set<Whirlwind> whirlwinds) {}
    record PuzzleSolution(int steps, Set<Whirlwind> whirlwinds) {}
}

/*



*/