package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec04 extends Christmas {

    long sampleAnswer1 = 2;
    long sampleAnswer2 = 4;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec04();
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

    private List<WorkPair> convertData(Stream<String> data) {
        return data
                .map(s -> mapToWorkPair(s))
                .collect(Collectors.toList());
    }

    private WorkPair mapToWorkPair(String line) {
        var parts = line.split(",");
        return new WorkPair(buildWorkSet(parts[0]), buildWorkSet(parts[1]));
    }

    private Set<Integer> buildWorkSet(String sectionRange) {
        var parts = sectionRange.split("-");
        var from = Integer.valueOf(parts[0]);
        var to = Integer.valueOf(parts[1]);
        var workSet = new TreeSet<Integer>();
        for (int i=from; i<=to; i++)
            workSet.add(i);
        return workSet;
    }

    public long solve1(Stream<String> stream) {
        var data = convertData(stream);
        var overlapCount = 0;
        for (var wp: data)
            if (totalOverlap(wp.a, wp.b))
                overlapCount++;
        return overlapCount;
    }

    private boolean totalOverlap(Set<Integer> a, Set<Integer> b) {
        return minus(a, b).isEmpty() || minus(b,a).isEmpty();
    }

    private Set<Integer> minus(Set<Integer> a, Set<Integer> b) {
        var from = new TreeSet<Integer>();
        from.addAll(a);
        from.removeAll(b);
        return from;
    }

    public long solve2(Stream<String> stream) {
        var data = convertData(stream);
        var overlapCount = 0;
        for (var wp: data)
            if (anyOverlap(wp.a, wp.b))
                overlapCount++;
        return overlapCount;
    }

    private boolean anyOverlap(Set<Integer> a, Set<Integer> b) {
        return plus(a, b).size() != a.size() + b.size();
    }

    private Set<Integer> plus(Set<Integer> a, Set<Integer> b) {
        var from = new TreeSet<Integer>();
        from.addAll(a);
        from.addAll(b);
        return from;
    }

    record WorkPair(Set <Integer> a, Set<Integer> b) {}
}

/*



*/