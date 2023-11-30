package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec22 extends Christmas {

    long sampleAnswer1 = 6032;
    long sampleAnswer2 = 5031;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec22();
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
    		new Pos(0, 1),
    		new Pos(1, 0),
    		new Pos(0, -1),
    		new Pos(-1, 0)
    };

    private Puzzle convertData(Stream<String> data) {
    	var lines = data.toList();
    	var moves = lines.get(lines.size() - 1);
    	var board = new char[lines.size() - 2][];
    	for (int i=0; i<lines.size() - 2; i++)
    		board[i] = lines.get(i).toCharArray();
    	var startCol = lines.get(0).indexOf('.');
        return new Puzzle(board, 0, startCol, moves);
    }

    public long solve1(Stream<String> stream) {
    	Puzzle puzzle = convertData(stream);
    	int dir = 0;
    	int steps = 0;
    	var currPos = new Pos(puzzle.startRow, puzzle.startCol);
    	for (var c: puzzle.moves.toCharArray())
    		switch (c) {
    		case 'L':
    			currPos = move(steps, dirs[dir], currPos, puzzle.board);
    			steps = 0;
    			dir -= 1;
    			if (dir < 0) dir += dirs.length;
    			break;
    		case 'R':
    			currPos = move(steps, dirs[dir], currPos, puzzle.board);
    			steps = 0;
    			dir += 1;
    			if (dir >= dirs.length) dir -= dirs.length;
    			break;
    		default: steps = steps * 10 + (c - '0');
    		}
        return 1000 * (currPos.row + 1) + 4 * (currPos.col + 1) + dir;
    }

    private Pos move(int steps, Pos pos, Pos currPos, char[][] board) {
    	for (int i=0; i<steps; i++) {
    		Pos nextPos = move(pos, currPos, board);
    		if (nextPos == currPos) return currPos;
    		currPos = nextPos;
    	}
    	return currPos;
	}

	private Pos move(Pos pos, Pos currPos, char[][] board) {
		Pos skipPos = currPos;
		while (true) {
			int nextRow = (skipPos.row + pos.row) % board.length;
			if (nextRow < 0) nextRow += board.length;
			int nextCol = (skipPos.col + pos.col) % board[nextRow].length;
			if (nextCol < 0) nextCol += board[nextRow].length;
			var nextPos = new Pos(nextRow, nextCol);
			if (board[nextPos.row][nextPos.col] == '.') return nextPos;
			if (board[nextPos.row][nextPos.col] == '#') return currPos;
			skipPos = nextPos;
		}
	}

	public long solve2(Stream<String> stream) {
        return 0;
    }

    record Pos(int row, int col) {}
    record Puzzle(char[][] board, int startRow, int startCol, String moves) {}
}

/*



*/