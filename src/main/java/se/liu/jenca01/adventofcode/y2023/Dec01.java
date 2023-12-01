package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec01 extends Christmas {

    long sampleAnswer1 = 142;
    long sampleAnswer2 = 142;

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

    private List<Long> convertData1(Stream<String> data) {
        return data.map(s -> lineToNumber1(s)).collect(Collectors.toList());
    }

    public long solve1(Stream<String> stream) {
        return convertData1(stream).stream().mapToLong(l -> l).sum();
    }

    private long lineToNumber1(String l) {
        return firstNum(l) * 10 + lastNum(l);
    }

    private int firstNum(String l) {
        for(var c: l.toCharArray()) {
            if (c < '0') continue;
            if (c > '9') continue;
            return c-'0';
        }

        throw new RuntimeException("huh? " + l);
    }

    private int lastNum(String l) {
        for(var c: new StringBuilder(l).reverse().toString().toCharArray()) {
            if (c < '0') continue;
            if (c > '9') continue;
            return c-'0';
        }

        throw new RuntimeException("huh? " + l);
    }

    private List<Long> convertData2(Stream<String> data) {
        return data.map(s -> lineToNumber2(s)).collect(Collectors.toList());
    }

    public long solve2(Stream<String> stream) {
        return convertData2(stream).stream().mapToLong(l -> l).sum();
    }

    private long lineToNumber2(String l) {
        return firstNum2(l) * 10 + lastNum2(l);
    }

    private int firstNum2(String l) {
        for(int i=0; i<l.length(); i++) {
            if (l.substring(i).startsWith("one")) return 1;
            if (l.substring(i).startsWith("two")) return 2;
            if (l.substring(i).startsWith("three")) return 3;
            if (l.substring(i).startsWith("four")) return 4;
            if (l.substring(i).startsWith("five")) return 5;
            if (l.substring(i).startsWith("six")) return 6;
            if (l.substring(i).startsWith("seven")) return 7;
            if (l.substring(i).startsWith("eight")) return 8;
            if (l.substring(i).startsWith("nine")) return 9;
            char c=l.charAt(i);
            if (c < '0') continue;
            if (c > '9') continue;
            return c-'0';
        }

        throw new RuntimeException("huh? " + l);
    }

    private int lastNum2(String l) {
        for(int i=l.length()-1; i>=0; i--) {
            if (l.substring(i).startsWith("one")) return 1;
            if (l.substring(i).startsWith("two")) return 2;
            if (l.substring(i).startsWith("three")) return 3;
            if (l.substring(i).startsWith("four")) return 4;
            if (l.substring(i).startsWith("five")) return 5;
            if (l.substring(i).startsWith("six")) return 6;
            if (l.substring(i).startsWith("seven")) return 7;
            if (l.substring(i).startsWith("eight")) return 8;
            if (l.substring(i).startsWith("nine")) return 9;
            char c=l.charAt(i);
            if (c < '0') continue;
            if (c > '9') continue;
            return c-'0';
        }

        throw new RuntimeException("huh? " + l);
    }
}
