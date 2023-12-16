package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec15 extends Christmas {

    long sampleAnswer1 = 1320;
    long sampleAnswer2 = 145;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec15();
        christmas.solveSample();
        christmas.solveMy();
    }

    @Override
    public void solveSample() throws Exception {

        calcHash("HASH");

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

    private String[] convertData(Stream<String> data) {
        return data.toList().get(0).split(",");
    }

    public long solve1(Stream<String> stream) {
        var codes = convertData(stream);
        long sum = 0;
        for (var code: codes)
            sum += calcHash(code);
        return sum;
    }

    private int calcHash(String code) {
        int hash = 0;
        for (var c: code.toCharArray()) {
            hash += c;
            hash *= 17;
            hash %= 256;
        }
        return hash;
    }

    public long solve2(Stream<String> stream) {
        var codes = convertData(stream);
        var map = new HashMap<Integer, List<Pair<String, Integer>>>();
        for (var code: codes) {
            var label = code.split("[-=]")[0];
            var box = calcHash(label);
            var currentLenses = getCurrentLenses(box, map);
            if (code.contains("-"))
                removeLens(currentLenses, label);
            else
                addLens(currentLenses, label, code.charAt(code.length() - 1) - '0');
            map.put(box, currentLenses);
        }
        long sum = 0;
        for (int box=0; box<256; box++) {
            var lenses = map.get(box);
            if (lenses == null) lenses = new ArrayList<>();
            for (int lens=0; lens<lenses.size(); lens++)
                sum += (box+1)*(lens+1)*lenses.get(lens).getValue();
        }
        return sum;
    }

    private int contains(List<Pair<String, Integer>> currentLenses, String label) {
        for (int i=0; i<currentLenses.size(); i++)
            if (currentLenses.get(i).getKey().equals(label))
                return i;
        return -1;
    }

    private void removeLens(List<Pair<String, Integer>> currentLenses, String label) {
        int currentLensPos = contains(currentLenses, label);
        if (currentLensPos > -1)
            currentLenses.remove(currentLensPos);
    }

    private void addLens(List<Pair<String, Integer>> currentLenses, String label, int focalLength) {
        int currentLensPos = contains(currentLenses, label);
        var newLens = Pair.of(label, focalLength);
        if (currentLensPos == -1)
            currentLenses.add(newLens);
        else
            currentLenses.set(currentLensPos, newLens);
    }

    private List<Pair<String, Integer>> getCurrentLenses(int box, HashMap<Integer, List<Pair<String, Integer>>> map) {
        if (map.containsKey(box))
            return map.get(box);
        return new ArrayList<>();
    }
}
