package se.liu.jenca01.adventofcode.y2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec20 extends Christmas {

    long sampleAnswer1 = 35;
    long sampleAnswer2 = 0;

    class PuzzleInput {

        private String algo;
        private Set<Pair<Integer, Integer>> points;
        private int maxX;
        private int maxY;

        public PuzzleInput(String algo, Set<Pair<Integer, Integer>> points, int maxX, int maxY) {
            this.algo = algo;
            this.points = points;
            this.maxX = maxX;
            this.maxY = maxY;
        }
    }

    public static void main(String[] args) throws Exception {
        var christmas = new Dec20();
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

    private PuzzleInput convertData(Stream<String> data) {
        var lines = toList(data);
        var algo = lines.remove(0);
        lines.remove(0);
        var points = new HashSet<Pair<Integer, Integer>>();

        for (int y=0; y<lines.size(); y++)
            for(int x=0; x<lines.get(y).length(); x++)
                if (lines.get(y).charAt(x) == '#')
                    points.add(Pair.of(x, y));

        return new PuzzleInput(algo, points, lines.size(), lines.get(0).length());
    }

    public long solve1(Stream<String> stream) {
        var data = convertData(stream);

        for (int i=0; i<2; i++)
            // TODO: take care of that all points outside will be lit after iter 1 (and u lit after 2
            data.points = enhance(data, i);
        return data.points.size();
    }

    private Set<Pair<Integer, Integer>> enhance(PuzzleInput data, int i) {
        var newPoints = new HashSet<Pair<Integer, Integer>>();
        for(int x=0-i; x<data.maxX+i; x++)
            for(int y=0-i; y<data.maxY+i; y++) {
                int calcIndex = calcIndex(data, x, y, i);
                char charAt = data.algo.charAt(calcIndex);
                if (charAt == '#')
                    newPoints.add(Pair.of(x, y));
            }
        return newPoints;
    }

    private int calcIndex(PuzzleInput data, int x, int y, int i) {
        StringBuilder sb = new StringBuilder(9);
        for(int dy=-1; dy<2; dy++)
            for(int dx=-1; dx<2; dx++)
                sb.append(translatePoint(data, x+dx, y+dy, i));
        return Integer.parseInt(sb.toString(), 2);
    }

    private char translatePoint(PuzzleInput data, int px, int py, int i) {
        if (i % 2 == 1) {
            if (px<-i || px >=data.maxX+i) return '1';
            if (py<-i || py >=data.maxY+i) return '1';
        }
        return data.points.contains(Pair.of(px, py)) ? '1' : '0';
    }

    public long solve2(Stream<String> stream) {
        return 0;
    }
}

/*



*/