package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec06 extends Christmas {

    long sampleAnswer1 = 7;
    long sampleAnswer2 = 19;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec06();
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
        var data = stream.findFirst().get().toCharArray();
        return solve1(data);
    }

    public long solve1(char[] data) {
        for (int i=3; i<data.length; i++)
            if (allDiffers(4, i, data))
                return i+1;
        throw new RuntimeException();
    }

    private boolean allDiffers(int noToCompare, int lastPos, char[] data) {
        if (noToCompare > lastPos) return false;
        var compare = new TreeSet<Character>();
        for(int i=0; i<noToCompare; i++)
            compare.add(data[lastPos-i]);
        return compare.size() == noToCompare;
    }

    public long solve2(Stream<String> stream) {
        var data = stream.findFirst().get().toCharArray();
        var firstPos = solve1(data);
        for (int i=(int)firstPos; i<data.length; i++)
            if (allDiffers(14, i, data))
                return i+1;
        throw new RuntimeException();
    }
}

/*



*/