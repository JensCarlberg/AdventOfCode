package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec03 extends Christmas {

    long sampleAnswer1 = 157;
    long sampleAnswer2 = 70;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec03();
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

    public long solve1(Stream<String> stream) {
        return stream
                .map(s -> findDouble(s))
                .map(c -> findPrio(c))
                .reduce(0, Dec03::add);
    }

    public static int add(int a, int b) {
        return a + b;
    }

    private Integer findPrio(Character c) {
        var val = 0;
        if (c.charValue() > 'Z')
            val = c - 'a' + 1;
        else val = c - 'A' + 27;
        return val;
    }

    private Character findDouble(String things) {
        var first = things.substring(0, things.length() / 2).toCharArray();
        var second = things.substring(things.length() / 2);
        for(var c: first)
            if (second.contains(""+c))
                return c;
        throw new RuntimeException();
    }

    public long solve2(Stream<String> stream) {
        var allPacks = stream.collect(Collectors.toList());
        var index = 0;
        var sumPrio = 0;
        while (index < allPacks.size()) {
            Integer findPrio = findPrio(findCommon(allPacks.get(index++), allPacks.get(index++), allPacks.get(index++)));
            sumPrio += findPrio;
        }
        return sumPrio;
    }

    private Character findCommon(String pack1, String pack2, String pack3) {
        for(var c1: pack1.toCharArray())
            for(var c2: pack2.toCharArray())
                if (c1 == c2)
                    for(var c3: pack3.toCharArray())
                        if (c2 == c3) return c3;
        throw new RuntimeException();
    }
}

/*



*/