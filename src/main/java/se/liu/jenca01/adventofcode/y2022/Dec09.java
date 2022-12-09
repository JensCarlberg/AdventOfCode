package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec09 extends Christmas {

    long sampleAnswer1 = 88; // 13 for smaller example
    long sampleAnswer2 = 36;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec09();
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

    private List<Move> convertData(Stream<String> data) {
        var stepMap = new HashMap<String, Point>(4);
        stepMap.put("D", new Point(0, -1));
        stepMap.put("U", new Point(0, 1));
        stepMap.put("L", new Point(-1, 0));
        stepMap.put("R", new Point(1, 0));
        
        var lines = data.toList();
        var moves = new ArrayList<Move>();
        for (var line: lines) {
            var parts = line.split(" ");
            var moveP = stepMap.get(parts[0]);
            var steps = Integer.parseInt(parts[1]);
            moves.add(new Move(moveP, steps));
        }
        return moves;
    }

    public long solve1(Stream<String> stream) {
        var moves = convertData(stream);
        var head = new Point(0, 0);
        var tail = new Point(0, 0);
        var seen = new TreeSet<Point>();
        seen.add(tail);
        
        for (var move: moves) {
            for(int i=0; i<move.steps; i++) {
                head = head.add(move.dir);
                tail = moveTailTowardsHead(tail, head);
                seen.add(tail);
            }
        }

        return seen.size();
    }

    private Point moveTailTowardsHead(Point tail, Point head) {
        var diffX = head.x - tail.x;
        var diffY = head.y - tail.y;
        if (diffX < 2 && diffX > -2 && diffY < 2 && diffY > -2)
            return tail;

        var moveX = diffX == 0 ? 0 : diffX / Math.abs(diffX);
        var moveY = diffY == 0 ? 0 : diffY / Math.abs(diffY);
        
        return new Point(tail.x + moveX, tail.y + moveY);
    }

    public long solve2(Stream<String> stream) {
        var moves = convertData(stream);
        var head = new Point(0, 0);
        var tails = new Point[9];
        for (int i=0; i<9; i++)
            tails[i] = new Point(0, 0);
        var seen = new TreeSet<Point>();
        seen.add(tails[8]);
        
        for (var move: moves) {
            for(int i=0; i<move.steps; i++) {
                head = head.add(move.dir);
                tails[0] = moveTailTowardsHead(tails[0], head);
                for (int j=1; j<9; j++) {
                    tails[j] = moveTailTowardsHead(tails[j], tails[j-1]);
                }
                seen.add(tails[8]);
            }
        }

        return seen.size();
    }
    
    record Point(int x, int y) implements Comparable<Point> {
        @Override public int compareTo(Point o) {
            return Objects.compare(this, o, Comparator.comparing(Point::x).thenComparing(Point::y));
        }
        
        Point add(Point o) {
            return new Point(this.x + o.x, this.y + o.y);
        }
    }
    
    record Move(Point dir, int steps) { }
}

/*



*/