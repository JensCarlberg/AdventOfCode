package se.liu.jenca01.adventofcode.y2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import se.liu.jenca01.adventofcode.Christmas;

public class Dec05 extends Christmas {

    long sampleAnswer1 = 35;
    long sampleAnswer2 = 46;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec05();
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

    enum State {
        SEED,
        SEED_SOIL,
        SOIL_FERTILIZER,
        FERTILIZER_WATER,
        WATER_LIGHT,
        LIGHT_TEMP,
        TEMP_HUMIDITY,
        HUMIDITY_LOCATION;
    }

    public long solve1(Stream<String> stream) {
        var lines = convertData(stream);
        var currState = State.SEED;
        var seedsList = new ArrayList<Long>();
        var ranges = new TreeMap<State, List<Range>>();
        for (var state: State.values())
            ranges.put(state, new ArrayList<>());
        for (var line: lines) {
            switch (currState) {
            case SEED:
                if (line.strip().length() == 0)
                    currState = State.SEED_SOIL;
                else
                    getSeeds(line, seedsList);
                break;
            case SEED_SOIL:
                if (line.strip().length() == 0) currState = State.SOIL_FERTILIZER;
                else if (line.contains(":")) continue;
                else addRange(line, ranges.get(currState));
                break;
            case SOIL_FERTILIZER:
                if (line.strip().length() == 0) currState = State.FERTILIZER_WATER;
                else if (line.contains(":")) continue;
                else addRange(line, ranges.get(currState));
                break;
            case FERTILIZER_WATER:
                if (line.strip().length() == 0) currState = State.WATER_LIGHT;
                else if (line.contains(":")) continue;
                else addRange(line, ranges.get(currState));
                break;
            case WATER_LIGHT:
                if (line.strip().length() == 0) currState = State.LIGHT_TEMP;
                else if (line.contains(":")) continue;
                else addRange(line, ranges.get(currState));
                break;
            case LIGHT_TEMP:
                if (line.strip().length() == 0) currState = State.TEMP_HUMIDITY;
                else if (line.contains(":")) continue;
                else addRange(line, ranges.get(currState));
                break;
            case TEMP_HUMIDITY:
                if (line.strip().length() == 0) currState = State.HUMIDITY_LOCATION;
                else if (line.contains(":")) continue;
                else addRange(line, ranges.get(currState));
                break;
            case HUMIDITY_LOCATION:
                if (line.strip().length() == 0) continue;
                else if (line.contains(":")) continue;
                else addRange(line, ranges.get(currState));
                break;
            default:
                throw new RuntimeException();
            }
        }
        var locs = seedsList.stream()
        .map(s -> mapSrc(s, ranges.get(State.SEED_SOIL)))
        .map(s -> mapSrc(s, ranges.get(State.SOIL_FERTILIZER)))
        .map(s -> mapSrc(s, ranges.get(State.FERTILIZER_WATER)))
        .map(s -> mapSrc(s, ranges.get(State.WATER_LIGHT)))
        .map(s -> mapSrc(s, ranges.get(State.LIGHT_TEMP)))
        .map(s -> mapSrc(s, ranges.get(State.TEMP_HUMIDITY)))
        .map(s -> mapSrc(s, ranges.get(State.HUMIDITY_LOCATION)))
        .toList();
        return locs.stream().mapToLong(l -> l).min().getAsLong();
    }

    private long mapSrc(Long src, List<Range> ranges) {
        for (var range: ranges)
            if (range.isInRange(src))
                return range.src2dest(src);
        return src;
    }

    private void addRange(String line, List<Range> list) {
        list.add(Range.parseRange(line));
    }

    private void getSeeds(String line, ArrayList<Long> seedsList) {
        for(var seedNum: line.split(": ")[1].split(" +"))
            seedsList.add(Long.parseLong(seedNum.strip()));
    }

    public long solve2(Stream<String> stream) {
        var lines = convertData(stream);
        var currState = State.SEED;
        var seedsList = new ArrayList<Long>();
        var ranges = new TreeMap<State, List<Range>>();
        for (var state: State.values())
            ranges.put(state, new ArrayList<>());
        for (var line: lines) {
            switch (currState) {
            case SEED:
                if (line.strip().length() == 0)
                    currState = State.SEED_SOIL;
                else
                    getSeeds2(line, seedsList);
                break;
            case SEED_SOIL:
                if (line.strip().length() == 0) currState = State.SOIL_FERTILIZER;
                else if (line.contains(":")) continue;
                else addRange(line, ranges.get(currState));
                break;
            case SOIL_FERTILIZER:
                if (line.strip().length() == 0) currState = State.FERTILIZER_WATER;
                else if (line.contains(":")) continue;
                else addRange(line, ranges.get(currState));
                break;
            case FERTILIZER_WATER:
                if (line.strip().length() == 0) currState = State.WATER_LIGHT;
                else if (line.contains(":")) continue;
                else addRange(line, ranges.get(currState));
                break;
            case WATER_LIGHT:
                if (line.strip().length() == 0) currState = State.LIGHT_TEMP;
                else if (line.contains(":")) continue;
                else addRange(line, ranges.get(currState));
                break;
            case LIGHT_TEMP:
                if (line.strip().length() == 0) currState = State.TEMP_HUMIDITY;
                else if (line.contains(":")) continue;
                else addRange(line, ranges.get(currState));
                break;
            case TEMP_HUMIDITY:
                if (line.strip().length() == 0) currState = State.HUMIDITY_LOCATION;
                else if (line.contains(":")) continue;
                else addRange(line, ranges.get(currState));
                break;
            case HUMIDITY_LOCATION:
                if (line.strip().length() == 0) continue;
                else if (line.contains(":")) continue;
                else addRange(line, ranges.get(currState));
                break;
            default:
                throw new RuntimeException();
            }
        }
        var locs = seedsList.stream()
        .map(s -> mapSrc(s, ranges.get(State.SEED_SOIL)))
        .map(s -> mapSrc(s, ranges.get(State.SOIL_FERTILIZER)))
        .map(s -> mapSrc(s, ranges.get(State.FERTILIZER_WATER)))
        .map(s -> mapSrc(s, ranges.get(State.WATER_LIGHT)))
        .map(s -> mapSrc(s, ranges.get(State.LIGHT_TEMP)))
        .map(s -> mapSrc(s, ranges.get(State.TEMP_HUMIDITY)))
        .map(s -> mapSrc(s, ranges.get(State.HUMIDITY_LOCATION)))
        .toList();
        return locs.stream().mapToLong(l -> l).min().getAsLong();
    }

    private void getSeeds2(String line, ArrayList<Long> seedsList) {
        String[] seedInfo = line.split(": ")[1].split(" +");
        for (int seedPos = 0; seedPos < seedInfo.length; seedPos += 2)
            generateRange(Long.parseLong(seedInfo[seedPos]), Long.parseLong(seedInfo[seedPos + 1]), seedsList);
    }

    private void generateRange(long start, long count, List<Long> list) {
        for (int i=0; i<count; i++)
            list.add(start + i);
    }

    record Range(long destinationStart, long sourceStart, long length) {
        static Range parseRange(String range) {
            var rangeParts = range.split(" ");
            return new Range(
                    Long.parseLong(rangeParts[0]),
                    Long.parseLong(rangeParts[1]),
                    Long.parseLong(rangeParts[2]));
        }
        boolean isInRange(long src) {
            var diff = src - sourceStart;
            return diff >= 0 && diff < length;
        }
        long src2dest(long src) { return destinationStart + src - sourceStart; }
    }
}
