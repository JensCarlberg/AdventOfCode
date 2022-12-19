package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec19 extends Christmas {

    long sampleAnswer1 = 0;
    long sampleAnswer2 = 0;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec19();
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
        return 0;
    }

    public long solve2(Stream<String> stream) {
        return 0;
    }
}

/*



*/