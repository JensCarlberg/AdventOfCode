package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec01 extends Christmas {

    long sampleAnswer1 = 24000;
    long sampleAnswer2 = 45000;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec01();
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

    private List<Long> convertData(Stream<String> data) {
        return data.map(s -> Long.parseLong(s)).collect(Collectors.toList());
    }

    public long solve1(Stream<String> stream) {
        var data = stream.collect(Collectors.toList());
        int most = 0;
        int curr = 0;
        for(var line: data) {
            if (line.length() == 0) {
                if (most < curr) most = curr;
                curr = 0;
            } else {
                curr += Integer.parseInt(line);
            }
        }
        return most;
    }

    public long solve2(Stream<String> stream) {
        var data = stream.collect(Collectors.toList());
        var sums = new TreeSet<Integer>();
        int curr = 0;
        for(var line: data) {
            if (line.length() == 0) {
                sums.add(curr);
                curr = 0;
            } else {
                curr += Integer.parseInt(line);
            }
        }
        sums.add(curr);
        var sumsL = sums.stream().sorted((x, t) -> t.compareTo(x)).collect(Collectors.toList());
        return sumsL.get(0) + sumsL.get(1) + sumsL.get(2);
    }
}

/*



*/