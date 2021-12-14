package se.liu.jenca01.adventofcode.y2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.ImmutablePair;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec14 extends Christmas {

    long sampleAnswer1 = 1588;
    long sampleAnswer2 = 2188189693529L;

    String templateLine = "-";
    
    public static void main(String[] args) throws Exception {
        var christmas = new Dec14();
        christmas.solveSample();
        christmas.solveMy();
    }

    @Override
    public void solveSample() throws Exception {
        // Warmup
        solve1(sampleData());

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

    private ImmutablePair<Map<String,Long>,Map<String,String>> convertData(Stream<String> data) {
        var lines = toList(data);
        templateLine = lines.get(0);
        var template = new HashMap<String, Long>();
        var rules = new HashMap<String, String>();

        for(int i=0; i<templateLine.length() - 1; i++)
            addOne(template, templateLine.substring(i, i+2));
        for (int i=2; i<lines.size(); i++)
            rules.put(lines.get(i).substring(0,  2), lines.get(i).substring(6));

        return new ImmutablePair<Map<String, Long>, Map<String, String>>(template, rules);
    }

    private void addOne(HashMap<String, Long> template, String key) {
        addX(template, key, 1);
    }

    private void addX(Map<String, Long> map, String key, long x) {
        if (map.containsKey(key))
            map.put(key, map.get(key) + x);
        else
            map.put(key,  x);
    }

    public long solve1(Stream<String> stream) {
        var data = convertData(stream);
        var iter = data.getLeft();
        var rules = data.getRight();
        for(int i=0; i<10; i++)
            iter = insert(iter, rules);
        return diffMinMax(iter);
    }

    private Map<String, Long> insert(Map<String, Long> iter, Map<String, String> rules) {
        var nextIter = new HashMap<String, Long>();
        for(var key: rules.keySet())
            if (iter.containsKey(key)) {
                var count = iter.get(key);
                var ins = rules.get(key);
                var p0 = key.charAt(0);
                var p1 = key.charAt(1);
                addX(nextIter, p0 + ins, count);
                addX(nextIter, ins + p1, count);
            }

        return nextIter;
    }

    private long diffMinMax(Map<String, Long> iter) {
        var countMap = new HashMap<Character, Long>();
        for (var key: iter.keySet())
            addX(key.charAt(0), countMap, iter.get(key));

        addX(templateLine.charAt(templateLine.length() - 1), countMap, 1);

        var min = countMap.values().stream().sorted().findFirst().get();
        var max = countMap.values().stream().map(c -> c * -1).sorted().findFirst().get() * -1;
        return max - min;
    }

    private void addX(char c, HashMap<Character, Long> countMap, long x) {
        if (countMap.containsKey(c))
            countMap.put(c,  countMap.get(c) + x);
        else
            countMap.put(c,  x);
    }

    public long solve2(Stream<String> stream) {
        var data = convertData(stream);
        var iter = data.getLeft();
        var rules = data.getRight();
        for(int i=0; i<40; i++)
            iter = insert(iter, rules);
        return diffMinMax(iter);
    }
}

/*



*/