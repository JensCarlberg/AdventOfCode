package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec02 extends Christmas {

    long sampleAnswer1 = 8;
    long sampleAnswer2 = 2286;

    @SuppressWarnings("serial")
    Map<String, Integer> solve1for = new TreeMap<>() {{
        put("red", 12);
        put("green", 13);
        put("blue", 14);
    }};

    public static void main(String[] args) throws Exception {
        var christmas = new Dec02();
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

    private List<String> convertData(Stream<String> data) {
        return data.collect(Collectors.toList());
    }

    public long solve1(Stream<String> stream) {
        var data = convertData(stream);
        Map <Integer, List<Map<String, Integer>>> games = new TreeMap<>();
        for (var line: data)
            parseGame(line, games);
        int gameSum = 0;
        for (var game: games.keySet())
            if (isPossible(games.get(game), solve1for))
                gameSum += game;
        return gameSum;
    }

    private void parseGame(String line, Map<Integer, List<Map<String, Integer>>> games) {
        var parts = line.split(":");
        int gameId = Integer.parseInt(parts[0].strip().split(" ")[1]);
        var draws = parts[1].split(";");
        var drawsList = new ArrayList<Map<String, Integer>>();
        for (var draw: draws) {
            var drawColorCount = new TreeMap<String, Integer>();
            var drawColors = draw.split(",");
            for (var color: drawColors) {
                var colorParts = color.strip().split(" ");
                drawColorCount.put(colorParts[1], Integer.parseInt(colorParts[0]));
            }
            drawsList.add(drawColorCount);
        }
        games.put(gameId, drawsList);
    }

    private boolean isPossible(List<Map<String, Integer>> draws, Map<String, Integer> solveFor) {
        for (var draw: draws)
            for (var colorCount: draw.keySet())
                if (draw.get(colorCount) > solveFor.get(colorCount))
                    return false;
        return true;
    }

    public long solve2(Stream<String> stream) {
        var data = convertData(stream);
        Map <Integer, List<Map<String, Integer>>> games = new TreeMap<>();
        for (var line: data)
            parseGame(line, games);

        int gameSum = 0;
        for (var game: games.keySet())
            gameSum += gamePower(games.get(game));
        return gameSum;
    }

    private int gamePower(List<Map<String, Integer>> draws) {
        var maxColorCount = new TreeMap<String, Integer>();
        for (var draw: draws)
            for (var color: draw.keySet())
                addIfLarger(color, draw.get(color), maxColorCount);
        int pow = 1;
        for (var count: maxColorCount.values())
            pow *= count;
        return pow;
    }

    private void addIfLarger(String color, Integer count, TreeMap<String, Integer> maxColorCount) {
        if (!maxColorCount.containsKey(color) || maxColorCount.get(color) < count)
            maxColorCount.put(color, count);
    }
}
